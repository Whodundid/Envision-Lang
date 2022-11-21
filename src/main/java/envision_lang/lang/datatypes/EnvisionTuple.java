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
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.EArrayList;
import eutil.strings.EStringUtil;

/**
 * A script datatype that represents an immutable grouping of multiple objects.
 * Internally backed by a Java::ArrayList.
 * 
 * @author Hunter Bragg
 */
public class EnvisionTuple extends ClassInstance {
	
	/**
	 * Internal Array list.
	 */
	private final EArrayList<EnvisionObject> list = new EArrayList<>();
	
	/**
	 * If parameterized to hold a specific datatype, this is that type.
	 */
	private final IDatatype tuple_type;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionTuple() { this(StaticTypes.VAR_TYPE); }
	protected EnvisionTuple(IDatatype typeIn) {
		super(EnvisionTupleClass.TUPLE_CLASS);
		tuple_type = typeIn;
	}
	
	protected EnvisionTuple(IDatatype typeIn, EArrayList listIn) {
		super(EnvisionTupleClass.TUPLE_CLASS);
		tuple_type = typeIn;
		list.addAll(listIn);
	}
	
	protected EnvisionTuple(EnvisionTuple in) {
		super(EnvisionTupleClass.TUPLE_CLASS);
		tuple_type = in.tuple_type;
		list.addAll(in.list);
	}
	
	protected EnvisionTuple(EnvisionTuple in, EArrayList listIn) {
		super(EnvisionTupleClass.TUPLE_CLASS);
		tuple_type = in.tuple_type;
		list.addAll(listIn);
	}
	
	protected EnvisionTuple(EnvisionList listIn) {
		super(EnvisionTupleClass.TUPLE_CLASS);
		tuple_type = listIn.getListType();
		list.addAll(listIn.getInternalList());
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionTuple env_tuple && env_tuple.list == list);
	}
	
	@Override
	public EnvisionTuple copy() {
		//shallow copy
		var l = EnvisionTupleClass.newTuple(tuple_type);
		for (var o : list) {
			if (o.isPrimitive()) l.list.add(o.copy());
			else l.list.add(o);
		}
		return l;
	}
	
	@Override
	public String toString() {
		return "(" + EStringUtil.combineAll(list, ", ") + ")";
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
			newTuple.list.add(toAdd);
			
			return newTuple;
		}
		else if (op == Operator.MUL) {
			if (!(obj instanceof EnvisionInt)) throw new InvalidArgumentError("Expected an integer value here!");
			EnvisionInt mulValue = (EnvisionInt) obj;
			long byAmount = mulValue.int_val;
			
			if (byAmount < 1) {
				throw new InvalidArgumentError("Expected an integer greater than or equal to '1' here!");
			}
			
			EArrayList<EnvisionObject> toCopy = new EArrayList<>(list.size());
			for (var o : list) toCopy.add(o);
			
			EnvisionTuple newTuple = new EnvisionTuple(this);
			
			for (int i = 0; i < (byAmount - 1); i++) {
				for (var o : toCopy) {
					if (o.isPrimitive()) newTuple.list.add(o.copy());
					else newTuple.list.add(o);
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
		case "getListType" -> getTupleTypeString();
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
	
	public EArrayList<EnvisionObject> getInternalList() { return list; }
	
	public EnvisionInt size() { return EnvisionIntClass.newInt(list.size()); }
	public long size_i() { return list.size(); }
	public boolean isEmpty_i() { return list.isEmpty(); }
	public EnvisionBoolean isEmpty() { return (list.isEmpty()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean hasOne() { return (list.hasOne()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean isNotEmpty() { return (list.isNotEmpty()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	
	public IDatatype getTupleType() { return tuple_type; }
	public EnvisionString getTupleTypeString() { return EnvisionStringClass.newString(tuple_type); }
	public EnvisionBoolean contains(EnvisionObject o) { return (list.contains(o)) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean notContains(EnvisionObject o) { return list.notContains(o) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	
	//-------------------
	// Tuple Get Methods
	//-------------------
	
	public EnvisionObject get(EnvisionInt index) { return get((int) index.int_val); }
	public EnvisionObject get(long index) { return get((int) index); }
	public EnvisionObject get(int index) { return list.get(checkEmpty(checkIndex(index))); }
	
	public EnvisionObject getFirst() { checkEmpty(); return list.getFirst(); }
	public EnvisionObject getLast() { checkEmpty(); return list.getLast(); }
	
	//---------------
	// Tuple Methods
	//---------------
	
	public EnvisionTuple flip() {
		return new EnvisionTuple(tuple_type, list.flip());
	}
	
	public EnvisionTuple shuffle() {
		EArrayList<EnvisionObject> l = new EArrayList<>(list);
		Collections.shuffle(l);
		return new EnvisionTuple(this, l);
	}
	
	public EnvisionObject random() {
		if (isEmpty_i()) return EnvisionNull.NULL;
		return list.getRandom();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void checkEmpty() { checkEmpty(0); }
	private int checkEmpty(int index) {
		if (list.isEmpty()) throw new EmptyTupleError(this);
		return index;
	}
	
	private int checkIndex(int index) {
		if (index < 0 || index >= list.size()) throw new IndexOutOfBoundsError(index, this);
		return index;
	}
	
	//---------------------------------------
	
	/**
	 * Creates an empty tuple.
	 * 
	 * @return The new tuple
	 */
	public static EnvisionTuple empty() {
		return new EnvisionTuple();
	}
	
	public static EnvisionTuple of(EnvisionObject[] vals) {
		EnvisionTuple tuple = new EnvisionTuple();
		tuple.list.addA(vals);
		return tuple;
	}
	
	public static <E extends EnvisionObject> EnvisionTuple of(IDatatype typeIn, E... vals) {
		EnvisionTuple tuple = new EnvisionTuple(typeIn);
		tuple.list.addA(vals);
		return tuple;
	}
	
}