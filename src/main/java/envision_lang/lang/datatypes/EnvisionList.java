package envision_lang.lang.datatypes;

import java.util.Collections;
import java.util.List;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.ArgLengthError;
import envision_lang.exceptions.errors.InvalidArgumentError;
import envision_lang.exceptions.errors.NoOverloadError;
import envision_lang.exceptions.errors.listErrors.EmptyListError;
import envision_lang.exceptions.errors.listErrors.IndexOutOfBoundsError;
import envision_lang.exceptions.errors.listErrors.LockedListError;
import envision_lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.internal.FunctionPrototype;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.StaticTypes;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

/**
 * A script variable that represents a grouping of multiple objects in the form
 * of an expandable list. Internally backed by a Java::ArrayList.
 * 
 * @author Hunter Bragg
 */
public class EnvisionList extends ClassInstance {
	
	/**
	 * Internal Array list.
	 */
	private final EArrayList<EnvisionObject> list = new EArrayList<>();
	
	/**
	 * If parameterized to hold a specific datatype, this is that type.
	 */
	private final IDatatype list_type;
	
	/**
	 * Prevents size modifications to this list.
	 */
	private boolean sizeLocked = false;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionList() { this(StaticTypes.VAR_TYPE); }
	protected EnvisionList(IDatatype typeIn) {
		super(EnvisionListClass.LIST_CLASS);
		list_type = typeIn;
	}
	
	protected EnvisionList(IDatatype typeIn, EArrayList listIn) {
		super(EnvisionListClass.LIST_CLASS);
		list_type = typeIn;
		list.addAll(listIn);
	}
	
	protected EnvisionList(EnvisionList in) {
		super(EnvisionListClass.LIST_CLASS);
		list_type = in.list_type;
		list.addAll(in.list);
	}
	
	protected EnvisionList(EnvisionList in, EArrayList listIn) {
		super(EnvisionListClass.LIST_CLASS);
		list_type = in.list_type;
		list.addAll(listIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EnvisionList env_list && env_list.list == list);
	}
	
	@Override
	public EnvisionList copy() {
		//shallow copy
		return new EnvisionList(this);
	}
	
	@Override
	public String toString() {
		return "[" + StringUtil.combineAll(list, ", ") + "]";
	}
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case EQUALS, NOT_EQUALS -> true;
		case ADD -> true;
		default -> false;
		};
	}
	
	@Override
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//Special case -- EnvisionLists do natively support null additions
		
		//only support '+=' operator
		if (op != Operator.ADD_ASSIGN) {
			return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
		}
		
		//attempt to add the incoming object to this list
		add(obj);
		
		return this;
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
		case "remove" -> remove((EnvisionInt) args[0]);
		case "removeFirst" -> removeFirst();
		case "removeLast" -> removeLast();
		case "set" -> set((EnvisionInt) args[0], args[1]);
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
	
	//---------
	// Methods
	//---------
	
	public EArrayList<EnvisionObject> getList() { return list; }
	
	public EnvisionInt size() { return EnvisionIntClass.newInt(list.size()); }
	public long size_i() { return list.size(); }
	public EnvisionBoolean isEmpty() { return (list.isEmpty()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean hasOne() { return (list.hasOne()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean isNotEmpty() { return (list.isNotEmpty()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	
	public IDatatype getListType() { return list_type; }
	public EnvisionString getListTypeString() { return EnvisionStringClass.newString(list_type); }
	public EnvisionBoolean contains(EnvisionObject o) { return (list.contains(o)) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean notContains(EnvisionObject o) { return list.notContains(o) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	
	//------------------
	// List Add Methods
	//------------------
	
	public EnvisionList add(EnvisionObject... val) {
		if (sizeLocked) throw lockedError();
		list.addA(val);
		return this;
	}
	
	public EnvisionList addAll(List<EnvisionObject> vals) {
		if (sizeLocked) throw lockedError();
		list.addAll(vals);
		return this;
	}
	
	public EnvisionList addAll(EnvisionList listIn) {
		if (sizeLocked) throw lockedError();
		list.addAll(listIn.list);
		return this;
	}
	
	public EnvisionBoolean add(EnvisionObject o) {
		if (sizeLocked) throw lockedError();
		return (list.add(o)) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE;
	}
	
	public EnvisionObject addR(EnvisionObject o) {
		if (sizeLocked) throw lockedError();
		return list.addR(o);
	}
	
	public EnvisionList addRT(EnvisionObject o) {
		if (sizeLocked) throw lockedError();
		list.addR(o);
		return this;
	}
	
	//------------------
	// List Get Methods
	//------------------
	
	public EnvisionObject get(EnvisionInt index) { return get((int) index.int_val); }
	public EnvisionObject get(long index) { return get((int) index); }
	public EnvisionObject get(int index) { return list.get(checkEmpty(checkIndex(index))); }
	
	public EnvisionObject getFirst() { checkEmpty(); return list.getFirst(); }
	public EnvisionObject getLast() { checkEmpty(); return list.getLast(); }
	
	//---------------------
	// List Remove Methods
	//---------------------
	
	public EnvisionObject remove(EnvisionInt index) { return remove(index.int_val); }
	public EnvisionObject remove(long index) { return remove((int) index); }
	public EnvisionObject remove(int index) {
		if (sizeLocked) throw lockedError();
		return list.remove(checkEmpty(checkIndex(index)));
	}
	
	public EnvisionObject removeFirst() {
		if (sizeLocked) throw lockedError();
		return list.remove(checkEmpty(0));
	}
	
	public EnvisionObject removeLast() {
		if (sizeLocked) throw lockedError();
		return list.remove(checkEmpty(list.size() - 1));
	}
	
	//------------------
	// List Set Methods
	//------------------
	
	public EnvisionObject set(EnvisionInt index, EnvisionObject obj) {
		return list.set(checkIndex((int) index.int_val), obj);
	}
	
	public EnvisionObject set(long index, EnvisionObject obj) {
		return list.set(checkIndex((int) index), obj);
	}
	
	public EnvisionObject set(int index, EnvisionObject obj) {
		return list.set(checkIndex(index), obj);
	}
	
	public EnvisionObject setFirst(EnvisionObject obj) {
		return list.set(checkEmpty(0), obj);
	}
	
	public EnvisionObject setLast(EnvisionObject obj) {
		return list.set(checkEmpty(list.size() - 1), obj);
	}
	
	//-------------------
	// List Size Methods
	//-------------------
	
	public EnvisionList setSize(EnvisionObject[] args) {
		if (args.length == 2) return setSize(((EnvisionInt) args[0]).int_val, args[1]);
		if (args.length == 1) return setSize(((EnvisionInt) args[0]).int_val, null);
		throw new EnvisionLangError("Invalid argumets -- EnvisionList::setSize");
	}
	
	public EnvisionList setSize(EnvisionObject sizeObj, EnvisionObject defaultValue) {
		if (sizeObj instanceof EnvisionInt env_int) return setSize(env_int.int_val, defaultValue);
		throw new InvalidArgumentError("Expected an integer for size!");
	}
	
	public EnvisionList setSize(long size, EnvisionObject defaultValue) {
		list.clear();
		for (int i = 0; i < size; i++) list.add(defaultValue);
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
		list.clear();
		return this;
	}
	
	public EnvisionList flip() {
		return new EnvisionList(list_type, list.flip());
	}
	
	public EnvisionList shuffle() {
		EArrayList l = new EArrayList(list);
		Collections.shuffle(l);
		return new EnvisionList(this, l);
	}
	
	public EnvisionList swap(EnvisionInt a, EnvisionInt b) { return swap(a.int_val, b.int_val); }
	public EnvisionList swap(long indexA, long indexB) { return swap((int) indexA, (int) indexB); }
	public EnvisionList swap(int indexA, int indexB) {
		checkEmpty();
		list.swap(checkIndex(indexA), checkIndex(indexB));
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
		return new EnvisionList(this, list.shiftLeft(amount));
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
		return new EnvisionList(this, list.shiftRight(amount));
	}
	
	/**
	 * I've got no idea what this is right now..
	 * @param args
	 * @return
	 */
	public EnvisionList fill(EnvisionObject[] args) {
		System.out.println(args);
		return this;
	}
	
	public EnvisionObject pop() {
		if (sizeLocked) throw lockedError();
		return list.pop();
	}
	
	public EnvisionList push(EnvisionObject o) {
		if (sizeLocked) throw lockedError();
		list.push(o);
		return this;
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void checkEmpty() { checkEmpty(0); }
	private int checkEmpty(int index) {
		if (list.isEmpty()) throw new EmptyListError(this);
		return index;
	}
	
	private int checkIndex(int index) {
		if (index < 0 || index >= list.size()) throw new IndexOutOfBoundsError(index, this);
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