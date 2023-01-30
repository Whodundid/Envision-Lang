package envision_lang.lang.datatypes;

import java.util.Collections;

import envision_lang.exceptions.errors.InvalidArgumentError;
import envision_lang.exceptions.errors.NoOverloadError;
import envision_lang.exceptions.errors.listErrors.EmptyTupleError;
import envision_lang.exceptions.errors.listErrors.IndexOutOfBoundsError;
import envision_lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.internal.FunctionPrototype;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

/**
 * A script datatype that represents an immutable grouping of multiple objects.
 * Internally backed by a Java::ArrayList.
 * 
 * @author Hunter Bragg
 */
public final class EnvisionTuple extends ClassInstance {
	
	public static final EnvisionTuple EMPTY_TUPLE = EnvisionTupleClass.newTuple();
	
	//========
	// Fields
	//========
	
	/**
	 * Internal Array list.
	 */
	public final EList<EnvisionObject> internal_list;
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionTuple() {
		super(EnvisionTupleClass.TUPLE_CLASS);
		internal_list = new EArrayList<>(0);
	}
	
	EnvisionTuple(EList listIn) {
		super(EnvisionTupleClass.TUPLE_CLASS);
		internal_list = new EArrayList<>(listIn);
	}
	
	EnvisionTuple(EnvisionTuple in) {
		super(EnvisionTupleClass.TUPLE_CLASS);
		internal_list = new EArrayList<>(in.internal_list);
	}
	
	EnvisionTuple(EnvisionList listIn) {
		super(EnvisionTupleClass.TUPLE_CLASS);
		internal_list = new EArrayList<>(listIn.getInternalList());
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionTuple env_tuple && env_tuple.internal_list == internal_list);
	}
	
	@Override
	public EnvisionTuple copy() {
		if (this == EMPTY_TUPLE) return EMPTY_TUPLE;
		
		//shallow copy
		var l = EnvisionTupleClass.newTuple();
		for (var o : internal_list) {
			if (o.isPrimitive()) l.internal_list.add(o.copy());
			else l.internal_list.add(o);
		}
		return l;
	}
	
	@Override
	public String toString() {
		return "(" + EStringUtil.combineAll(internal_list, ", ") + ")";
	}
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case EQUALS, NOT_EQUALS -> true;
		case MUL -> true;
		case ADD_ASSIGN -> true;
		default -> false;
		};
	}
	
	@Override
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//Special case -- EnvisionLists do natively support null additions
		
		//support '+=' operator
		if (op == Operator.ADD_ASSIGN) {
			EnvisionObject toAdd = obj;
			
			//attempt to add the incoming object to this list
			if (obj instanceof EnvisionVariable var) toAdd = var.copy();
			
			EnvisionTuple newTuple = new EnvisionTuple(this);
			newTuple.internal_list.add(toAdd);
			
			return newTuple;
		}
		else if (op == Operator.MUL) {
			if (!(obj instanceof EnvisionInt)) throw new InvalidArgumentError("Expected an integer value here!");
			EnvisionInt mulValue = (EnvisionInt) obj;
			long byAmount = mulValue.int_val;
			
			if (byAmount < 1) {
				throw new InvalidArgumentError("Expected an integer greater than or equal to '1' here!");
			}
			
			EArrayList<EnvisionObject> toCopy = new EArrayList<>(internal_list.size());
			for (var o : internal_list) toCopy.add(o);
			
			EnvisionTuple newTuple = new EnvisionTuple(this);
			
			for (int i = 0; i < (byAmount - 1); i++) {
				for (var o : toCopy) {
					if (o.isPrimitive()) newTuple.internal_list.add(o.copy());
					else newTuple.internal_list.add(o);
				}
			}
			
			return newTuple;
		}
		
		return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
	}
	
	@Override
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
		
		return switch (funcName) {
		case "contains" -> contains(args[0]);
		case "copy" -> copy();
		case "get" -> get((EnvisionInt) args[0]);
		case "getFirst" -> getFirst();
		case "getLast" -> getLast();
		case "hasOne" -> hasOne();
		case "isEmpty" -> isEmpty();
		case "isNotEmpty" -> isNotEmpty();
		case "notContains" -> notContains(args[0]);
		case "random" -> random();
		case "shuffle" -> shuffle();
		case "size" -> size();
		default -> super.handlePrimitive(proto, args);
		};
	}
	
	//---------
	// Methods
	//---------
	
	public EList<EnvisionObject> getInternalList() { return internal_list; }
	
	public void add(EnvisionObject o) { internal_list.add(o); }
	
	public EnvisionInt size() { return EnvisionIntClass.valueOf(internal_list.size()); }
	public long size_i() { return internal_list.size(); }
	
	public EnvisionBoolean isEmpty() { return EnvisionBooleanClass.valueOf(internal_list.isEmpty()); }
	public boolean isEmpty_i() { return internal_list.isEmpty(); }
	
	public EnvisionBoolean hasOne() { return EnvisionBooleanClass.valueOf(internal_list.hasOne()); }
	public EnvisionBoolean isNotEmpty() { return EnvisionBooleanClass.valueOf(internal_list.isNotEmpty()); }
	
	public EnvisionBoolean contains(EnvisionObject o) { return EnvisionBooleanClass.valueOf(internal_list.contains(o)); }
	public EnvisionBoolean notContains(EnvisionObject o) { return EnvisionBooleanClass.valueOf(internal_list.notContains(o)); }
	
	//-------------------
	// Tuple Get Methods
	//-------------------
	
	public EnvisionObject get(EnvisionInt index) { return get((int) index.int_val); }
	public EnvisionObject get(long index) { return get((int) index); }
	public EnvisionObject get(int index) { return internal_list.get(checkEmpty(checkIndex(index))); }
	
	public EnvisionObject getFirst() { checkEmpty(); return internal_list.getFirst(); }
	public EnvisionObject getLast() { checkEmpty(); return internal_list.getLast(); }
	
	//---------------
	// Tuple Methods
	//---------------
	
	public EnvisionTuple flip() {
		if (this == EMPTY_TUPLE) return EMPTY_TUPLE;
		return new EnvisionTuple(internal_list.reverse());
	}
	
	public EnvisionTuple shuffle() {
		if (this == EMPTY_TUPLE) return EMPTY_TUPLE;
		EList<EnvisionObject> l = EList.newList(internal_list);
		Collections.shuffle(l);
		return new EnvisionTuple(l);
	}
	
	public EnvisionObject random() {
		if (isEmpty_i()) return EnvisionNull.NULL;
		return internal_list.getRandom();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void checkEmpty() { checkEmpty(0); }
	private int checkEmpty(int index) {
		if (internal_list.isEmpty()) throw new EmptyTupleError(this);
		return index;
	}
	
	private int checkIndex(int index) {
		if (index < 0 || index >= internal_list.size()) throw new IndexOutOfBoundsError(index, this);
		return index;
	}
	
	//---------------------------------------
	
	/**
	 * Creates an empty tuple.
	 */
	public static EnvisionTuple empty() {
		return EMPTY_TUPLE;
	}
	
	public static EnvisionTuple of(EnvisionObject obj) {
		EnvisionTuple tuple = EnvisionTupleClass.newTuple();
		tuple.internal_list.add(obj);
		return tuple;
	}
	
	public static EnvisionTuple of(EnvisionObject a, EnvisionObject b) {
		EnvisionTuple tuple = EnvisionTupleClass.newTuple();
		var list = tuple.internal_list;
		list.add(a);
		list.add(b);
		return tuple;
	}
	
	public static EnvisionTuple of(EnvisionObject a, EnvisionObject b, EnvisionObject c) {
		EnvisionTuple tuple = EnvisionTupleClass.newTuple();
		var list = tuple.internal_list;
		list.add(a);
		list.add(b);
		list.add(c);
		return tuple;
	}
	
	public static EnvisionTuple of(EnvisionObject... vals) {
		EnvisionTuple tuple = new EnvisionTuple();
		tuple.internal_list.addA(vals);
		return tuple;
	}
	
}