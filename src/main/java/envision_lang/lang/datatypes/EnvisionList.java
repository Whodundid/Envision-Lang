package envision_lang.lang.datatypes;

import java.util.Collections;
import java.util.List;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.functions.FunctionPrototype;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.language_errors.error_types.ArgLengthError;
import envision_lang.lang.language_errors.error_types.InvalidArgumentError;
import envision_lang.lang.language_errors.error_types.NoOverloadError;
import envision_lang.lang.language_errors.error_types.listErrors.EmptyListError;
import envision_lang.lang.language_errors.error_types.listErrors.IndexOutOfBoundsError;
import envision_lang.lang.language_errors.error_types.listErrors.LockedListError;
import envision_lang.lang.language_errors.error_types.objects.UnsupportedOverloadError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

/**
 * A script variable that represents a grouping of multiple objects in the form
 * of an expandable list. Internally backed by a Java::ArrayList.
 * 
 * @author Hunter Bragg
 */
public final class EnvisionList extends ClassInstance {
	
	public static final IDatatype LIST_TYPE = EnvisionStaticTypes.LIST_TYPE;
	
	//========
	// Fields
	//========
	
	/**
	 * Internal Array list.
	 */
	public final EList<EnvisionObject> internalList;
	
	/**
	 * If parameterized to hold a specific datatype, this is that type.
	 */
	private final IDatatype listComponentType;
	
	/**
	 * Prevents size modifications to this list.
	 */
	private boolean sizeLocked = false;
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionList() { this(EnvisionStaticTypes.VAR_TYPE); }
	EnvisionList(IDatatype typeIn) {
		super(EnvisionListClass.LIST_CLASS);
		listComponentType = typeIn;
		internalList = new EArrayList<>();
	}
	
	EnvisionList(IDatatype typeIn, EList<EnvisionObject> listIn) {
		super(EnvisionListClass.LIST_CLASS);
		listComponentType = typeIn;
		internalList = new EArrayList<>(listIn);
	}
	
	EnvisionList(EnvisionList in) {
		super(EnvisionListClass.LIST_CLASS);
		listComponentType = in.listComponentType;
		internalList = new EArrayList<>(in.internalList);
	}
	
	EnvisionList(EnvisionList in, EList<EnvisionObject> listIn) {
		super(EnvisionListClass.LIST_CLASS);
		listComponentType = in.listComponentType;
		internalList = new EArrayList<>(listIn);
	}
	
	EnvisionList(EnvisionTuple tupleIn) {
		super(EnvisionListClass.LIST_CLASS);
		listComponentType = EnvisionStaticTypes.VAR_TYPE;
		internalList = new EArrayList<>(tupleIn.getInternalList());
	}
	
	EnvisionList(int initialSizeIn) {
		super(EnvisionListClass.LIST_CLASS);
		listComponentType = EnvisionStaticTypes.VAR_TYPE;
		internalList = new EArrayList<>(initialSizeIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionList env_list && env_list.internalList == internalList);
	}
	
	@Override
	public EnvisionList copy() {
		//shallow copy
		var l = EnvisionListClass.newList(listComponentType);
		for (var o : internalList) {
			if (o.isPrimitive()) l.add(o.copy());
			else l.add(o);
		}
		return l;
	}
	
	@Override
	public String toString() {
		return "[" + EStringUtil.combineAll(internalList, ", ") + "]";
	}
	
	public String convertToString(EnvisionInterpreter interpreter) {
		var sb = new EStringBuilder("[");
		int i = 0;
		for (EnvisionObject o : internalList) {
			if (o instanceof ClassInstance ci) {
				System.out.println(ci.getScope());
			}
			sb.a(EnvisionStringFormatter.formatPrint(interpreter, o), true);
			if (i < internalList.size()) sb.a(", ");
			i++;
		}
		sb.a("]");
		return sb.toString();
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
			//attempt to add the incoming object to this list
			if (obj instanceof EnvisionVariable<?> v) add(v.copy());
			else add(obj);
			
			return this;
		}
		else if (op == Operator.MUL) {
			if (!(obj instanceof EnvisionInt)) throw new InvalidArgumentError("Expected an integer value here!");
			EnvisionInt mulValue = (EnvisionInt) obj;
			long byAmount = mulValue.int_val;
			
			if (byAmount < 1) {
				throw new InvalidArgumentError("Expected an integer greater than or equal to '1' here!");
			}
			
			EList<EnvisionObject> toCopy = new EArrayList<>(internalList.size());
			for (var o : internalList) toCopy.add(o);
			
			if (internalList.isEmpty()) {
				for (int i = 0; i < byAmount; i++) {
					internalList.add(EnvisionNull.NULL);
				}
			}
			else {
				for (int i = 0; i < (byAmount - 1); i++) {
					for (var o : toCopy) {
						if (o.isPrimitive()) internalList.add(o.copy());
						else internalList.add(o);
					}
				}
			}
			
			return this;
		}
		

		
		return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
	}
	
	@Override
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
		
		return switch (funcName) {
		case "add" -> add(args[0]);
		case "addR" -> addR(args[0]);
		case "addRT" -> addRT(args[0]);
		case "clear" -> clear();
		case "contains" -> contains(args[0]);
		case "copy" -> copy();
		case "fill" -> fill(args);
		case "get" -> get((EnvisionInt) args[0]);
		case "getFirst" -> getFirst();
		case "getLast" -> getLast();
		case "getListType" -> getListTypeString();
		case "hasOne" -> hasOne();
		case "isEmpty" -> isEmpty();
		case "isNotEmpty" -> isNotEmpty();
		case "isSizeLocked" -> isSizeLocked();
		case "lockSize" -> lockSize();
		case "notContains" -> notContains(args[0]);
		case "push" -> push(args[0]);
		case "pop" -> pop();
		case "random" -> random();
		case "remove" -> remove((EnvisionInt) args[0]);
		case "removeFirst" -> removeFirst();
		case "removeLast" -> removeLast();
		case "removeRandom" -> removeRandom();
		case "reverse" -> reverse();
		case "set" -> set((EnvisionInt) args[0], args[1]);
		case "setSize" -> setSize(args);
		case "setFirst" -> setFirst(args[0]);
		case "setLast" -> setLast(args[0]);
		case "shiftLeft" -> shiftLeft(args);
		case "shiftRight" -> shiftRight(args);
		case "shuffle" -> shuffle();
		case "size" -> size();
		case "swap" -> swap((EnvisionInt) args[0], (EnvisionInt) args[1]);
		default -> super.handlePrimitive(proto, args);
		};
	}
	
	//=========
	// Methods
	//=========
	
	public EList<EnvisionObject> getInternalList() { return internalList; }
	
	public EnvisionInt size() { return EnvisionIntClass.valueOf(internalList.size()); }
	public long size_i() { return internalList.size(); }
	public boolean isEmpty_i() { return internalList.isEmpty(); }
	public EnvisionBoolean isEmpty() { return (internalList.isEmpty()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean hasOne() { return (internalList.hasOne()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean isNotEmpty() { return (internalList.isNotEmpty()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	
	public IDatatype getListType() { return listComponentType; }
	public EnvisionString getListTypeString() { return EnvisionStringClass.valueOf(listComponentType); }
	public EnvisionBoolean contains(EnvisionObject o) { return (internalList.contains(o)) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean notContains(EnvisionObject o) { return internalList.notContains(o) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	
	public EnvisionObject[] toArray() {
	    int size = (int) size_i();
	    EnvisionObject[] arr = new EnvisionObject[size];
	    for (int i = 0; i < size && i < Integer.MAX_VALUE; i++) {
	        arr[i] = internalList.get(i);
	    }
	    return arr;
	}
	
	//==================
	// List Add Methods
	//==================
	
	public EnvisionList add(EnvisionObject... val) {
		if (sizeLocked) throw lockedError();
		internalList.addA(val);
		return this;
	}
	
	public EnvisionList addAll(List<EnvisionObject> vals) {
		if (sizeLocked) throw lockedError();
		internalList.addAll(vals);
		return this;
	}
	
	public EnvisionList addAll(EnvisionList listIn) {
		if (sizeLocked) throw lockedError();
		internalList.addAll(listIn.internalList);
		return this;
	}
	
	public EnvisionBoolean add(EnvisionObject o) {
		if (sizeLocked) throw lockedError();
		return (internalList.add(o)) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE;
	}
	
	public EnvisionObject addR(EnvisionObject o) {
		if (sizeLocked) throw lockedError();
		return internalList.addR(o);
	}
	
	public EnvisionList addRT(EnvisionObject o) {
		if (sizeLocked) throw lockedError();
		internalList.addR(o);
		return this;
	}
	
	//------------------
	// List Get Methods
	//------------------
	
	public EnvisionObject get(EnvisionInt index) { return get((int) index.int_val); }
	public EnvisionObject get(long index) { return get((int) index); }
	public EnvisionObject get(int index) { return internalList.get(checkEmpty(checkIndex(index))); }
	
	public EnvisionObject getFirst() { checkEmpty(); return internalList.getFirst(); }
	public EnvisionObject getLast() { checkEmpty(); return internalList.getLast(); }
	
	//---------------------
	// List Remove Methods
	//---------------------
	
	public EnvisionObject remove(EnvisionInt index) { return remove(index.int_val); }
	public EnvisionObject remove(long index) { return remove((int) index); }
	public EnvisionObject remove(int index) {
		if (sizeLocked) throw lockedError();
		return internalList.remove(checkEmpty(checkIndex(index)));
	}
	
	public EnvisionObject removeFirst() {
		if (sizeLocked) throw lockedError();
		return internalList.remove(checkEmpty(0));
	}
	
	public EnvisionObject removeLast() {
		if (sizeLocked) throw lockedError();
		return internalList.remove(checkEmpty(internalList.size() - 1));
	}
	
	public EnvisionObject removeRandom() {
		if (sizeLocked) throw lockedError();
		if (internalList.isEmpty()) return EnvisionNull.NULL;
		return internalList.removeRandom();
	}
	
	//------------------
	// List Set Methods
	//------------------
	
	public EnvisionObject set(EnvisionInt index, EnvisionObject obj) {
		return internalList.set(checkIndex((int) index.int_val), obj);
	}
	
	public EnvisionObject set(long index, EnvisionObject obj) {
		return internalList.set(checkIndex((int) index), obj);
	}
	
	public EnvisionObject set(int index, EnvisionObject obj) {
		return internalList.set(checkIndex(index), obj);
	}
	
	public void setFast(int index, EnvisionObject obj) {
		internalList.set(index, obj);
	}
	
	public EnvisionObject setFirst(EnvisionObject obj) {
		return internalList.set(checkEmpty(0), obj);
	}
	
	public EnvisionObject setLast(EnvisionObject obj) {
		return internalList.set(checkEmpty(internalList.size() - 1), obj);
	}
	
	//-------------------
	// List Size Methods
	//-------------------
	
	public EnvisionList setSize(EnvisionObject[] args) {
		if (sizeLocked) throw lockedError();
		if (args.length == 2) return setSize(((EnvisionInt) args[0]).int_val, args[1]);
		if (args.length == 1) return setSize(((EnvisionInt) args[0]).int_val, null);
		throw new EnvisionLangError("Invalid argumets -- EnvisionList::setSize");
	}
	
	public EnvisionList setSize(EnvisionObject sizeObj, EnvisionObject defaultValue) {
		if (sizeLocked) throw lockedError();
		if (sizeObj instanceof EnvisionInt env_int) return setSize(env_int.int_val, defaultValue);
		throw new InvalidArgumentError("Expected an integer for size!");
	}
	
	public EnvisionList setSize(long size, EnvisionObject defaultValue) {
		if (sizeLocked) throw lockedError();
		internalList.clear();
		for (int i = 0; i < size; i++) internalList.add(defaultValue);
		return this;
	}
	
	public EnvisionBoolean isSizeLocked() {
		return (sizeLocked) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE;
	}
	
	public EnvisionList lockSize() {
		sizeLocked = true;
		return this;
	}
	
	public EnvisionList setSizeLocked(EnvisionBoolean val) {
		sizeLocked = val.bool_val;
		return this;
	}
	
	//-----------------------------------
	// List Content Modification Methods
	//-----------------------------------
	
	public EnvisionList clear() {
		if (sizeLocked) throw lockedError();
		internalList.clear();
		return this;
	}
	
	public EnvisionList reverse() {
		return new EnvisionList(listComponentType, internalList.reverse());
	}
	
	public EnvisionList shuffle() {
		EList<EnvisionObject> l = new EArrayList<>(internalList);
		Collections.shuffle(l);
		return new EnvisionList(this, l);
	}
	
	public EnvisionList swap(EnvisionInt a, EnvisionInt b) { return swap(a.int_val, b.int_val); }
	public EnvisionList swap(long indexA, long indexB) { return swap((int) indexA, (int) indexB); }
	public EnvisionList swap(int indexA, int indexB) {
		checkEmpty();
		internalList.swap(checkIndex(indexA), checkIndex(indexB));
		return this;
	}
	
	public EnvisionList shiftLeft(EnvisionObject[] args) {
		if (args.length == 0) return shiftLeft();
		if (args.length == 1) return shiftLeft((EnvisionInt) args[0]);
		else throw new ArgLengthError("shiftLeft", 1, args.length);
	}
	public EnvisionList shiftLeft() { return shiftLeft(1); }
	public EnvisionList shiftLeft(EnvisionInt intIn) { return shiftLeft((int) intIn.int_val); }
	public EnvisionList shiftLeft(long amount) { return shiftLeft((int) amount); }
	public EnvisionList shiftLeft(int amount) {
		return new EnvisionList(this, internalList.shiftLeft(amount));
	}
	
	public EnvisionList shiftRight(EnvisionObject[] args) {
		if (args.length == 0) return shiftRight();
		if (args.length == 1) return shiftRight((EnvisionInt) args[0]);
		else throw new ArgLengthError("shiftRight", 1, args.length);
	}
	public EnvisionList shiftRight() { return shiftRight(1); }
	public EnvisionList shiftRight(EnvisionInt intIn) { return shiftRight((int) intIn.int_val); }
	public EnvisionList shiftRight(long amount) { return shiftRight((int) amount); }
	public EnvisionList shiftRight(int amount) {
		return new EnvisionList(this, internalList.shiftRight(amount));
	}
	
	public EnvisionObject random() {
		if (isEmpty_i()) return EnvisionNull.NULL;
		return internalList.getRandom();
	}
	
	/**
	 * I've got no idea what this is right now..
	 * @param args
	 * @return
	 */
	public EnvisionList fill(EnvisionObject[] args) {
		if (args.length != 1) throw new ArgLengthError("List::fill", 1, args.length);
		for (int i = 0; i < internalList.size(); i++) {
			internalList.set(i, args[0]);
		}
		return this;
	}
	
	public EnvisionObject pop() {
		if (sizeLocked) throw lockedError();
		return internalList.pop();
	}
	
	public EnvisionList push(EnvisionObject o) {
		if (sizeLocked) throw lockedError();
		internalList.push(o);
		return this;
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void checkEmpty() { checkEmpty(0); }
	private int checkEmpty(int index) {
		if (internalList.isEmpty()) throw new EmptyListError(this);
		return index;
	}
	
	private int checkIndex(int index) {
		if (index < 0 || index >= internalList.size()) throw new IndexOutOfBoundsError(index, this);
		return index;
	}
	
	private LockedListError lockedError() {
		return new LockedListError(this);
	}
	
	//---------------------------------------
	
	/**
	 * Creates an empty list with the given parameterized datatype.
	 * 
	 * @param type The datatype of the list
	 * @return The new list
	 */
	public static EnvisionList empty(IDatatype type) {
		return new EnvisionList(type);
	}
	
	public static EnvisionList of(EnvisionObject[] vals) {
		EnvisionList list = new EnvisionList();
		list.add(vals);
		return list;
	}
	
	public static <E extends EnvisionObject> EnvisionList of(IDatatype typeIn, E... vals) {
		EnvisionList list = new EnvisionList(typeIn);
		list.add(vals);
		return list;
	}
	
}