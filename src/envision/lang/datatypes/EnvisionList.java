package envision.lang.datatypes;

import java.util.Collections;
import java.util.List;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.exceptions.errors.listErrors.EmptyListError;
import envision.exceptions.errors.listErrors.IndexOutOfBoundsError;
import envision.exceptions.errors.listErrors.LockedListError;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

public class EnvisionList extends ClassInstance {
	
	/**
	 * Internal Array list.
	 */
	private final EArrayList<EnvisionObject> list = new EArrayList<EnvisionObject>();
	
	/**
	 * If parameterized to hold a specific datatype, this is that type.
	 */
	private final EnvisionDatatype list_type;
	
	/**
	 * Prevents size modifications to this list.
	 */
	private boolean sizeLocked = false;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionList() { this(EnvisionDatatype.VAR_TYPE); }
	protected EnvisionList(Primitives typeIn) { this(typeIn.toDatatype()); }
	protected EnvisionList(EnvisionDatatype typeIn) {
		super(EnvisionListClass.LIST_CLASS);
		list_type = typeIn;
	}
	
	protected EnvisionList(Primitives typeIn, EArrayList listIn) {
		super(EnvisionListClass.LIST_CLASS);
		list_type = typeIn.toDatatype();
		list.addAll(listIn);
	}
	
	protected EnvisionList(EnvisionDatatype typeIn, EArrayList listIn) {
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
	public EnvisionList copy() {
		//shallow copy
		return new EnvisionList(this);
	}
	
	//--------------------------------------
	
	/*
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) { return new EnvisionInt(); }
		if (args.length == 1) {
			Object obj = convert(args[0]);
			
			if (obj instanceof EnvisionList) { return new EnvisionList((EnvisionList) obj); }
			if (obj instanceof Long) { return new EnvisionList().setSize((long) obj, new EnvisionNullObject()); }
		}
		if (args.length == 2) {
			Object a = convert(args[0]);
			Object b = convert(args[1]);
			
			if (a instanceof Long) { return new EnvisionList().setSize((long) a, b); }
		}
		return null;
	}
	*/
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "[" + StringUtil.combineAll(list, ", ") + "]";
	}
	
	//---------
	// Methods
	//---------
	
	public EArrayList getList() { return list; }
	
	public EnvisionInt size() { return EnvisionIntClass.newInt(list.size()); }
	public long size_i() { return list.size(); }
	public EnvisionBoolean isEmpty() { return (list.isEmpty()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean hasOne() { return (list.hasOne()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	public EnvisionBoolean isNotEmpty() { return (list.isNotEmpty()) ? EnvisionBoolean.TRUE : EnvisionBoolean.FALSE; }
	
	public EnvisionString getListType() { return EnvisionStringClass.newString(list_type); }
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
	
	public EnvisionList addAll(List vals) {
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
	
	public EnvisionObject get(EnvisionInt index) { return get((int) index.long_val); }
	public EnvisionObject get(long index) { return get((int) index); }
	public EnvisionObject get(int index) { return list.get(checkEmpty(checkIndex(index))); }
	
	public EnvisionObject getFirst() { checkEmpty(); return list.getFirst(); }
	public EnvisionObject getLast() { checkEmpty(); return list.getLast(); }
	
	//---------------------
	// List Remove Methods
	//---------------------
	
	public EnvisionObject remove(EnvisionInt index) { return remove(index.long_val); }
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
		return list.set(checkIndex((int) index.long_val), obj);
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
		if (args.length == 2) return setSize(((EnvisionInt) args[0]).long_val, args[1]);
		if (args.length == 1) return setSize(((EnvisionInt) args[0]).long_val, null);
		throw new EnvisionError("Invalid argumets -- EnvisionList::setSize");
	}
	
	public EnvisionList setSize(EnvisionObject sizeObj, EnvisionObject defaultValue) {
		if (sizeObj instanceof EnvisionInt env_int) return setSize(env_int.long_val, defaultValue);
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
	
	public EnvisionList swap(EnvisionInt a, EnvisionInt b) { return swap(a.long_val, b.long_val); }
	public EnvisionList swap(long indexA, long indexB) { return swap((int) indexA, (int) indexB); }
	public EnvisionList swap(int indexA, int indexB) {
		checkEmpty();
		list.swap(checkIndex(indexA), checkIndex(indexB));
		return this;
	}
	
	public EnvisionList shiftLeft() { return shiftLeft(1); }
	public EnvisionList shiftLeft(EnvisionInt intIn) { return shiftLeft((int) intIn.long_val); }
	public EnvisionList shiftLeft(long amount) { return shiftLeft((int) amount); }
	public EnvisionList shiftLeft(int amount) {
		return new EnvisionList(this, list.shiftLeft(amount));
	}
	
	public EnvisionList shiftRight() { return shiftRight(1); }
	public EnvisionList shiftRight(EnvisionInt intIn) { return shiftRight((int) intIn.long_val); }
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
	public static EnvisionList empty(EnvisionDatatype type) {
		return new EnvisionList(type);
	}
	
	public static EnvisionList of(EnvisionObject[] vals) {
		EnvisionList list = new EnvisionList();
		list.add(vals);
		return list;
	}
	
	public static <E extends EnvisionObject> EnvisionList of(EnvisionDatatype typeIn, E... vals) {
		EnvisionList list = new EnvisionList(typeIn);
		list.add(vals);
		return list;
	}
	
}
