package envision.lang.objects;

import static envision.lang.util.Primitives.*;

import envision.exceptions.errors.listErrors.EmptyListError;
import envision.exceptions.errors.listErrors.IndexOutOfBoundsError;
import envision.exceptions.errors.listErrors.LockedListError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.InternalFunction;
import envision.lang.util.Primitives;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;

import java.util.Collections;
import java.util.List;

public class EnvisionList extends EnvisionObject {

	private final EArrayList list = new EArrayList();
	private final EnvisionDatatype type;
	private boolean sizeLocked = false;
	private EnvisionList l;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionList() { this(EnvisionDatatype.prim_var(), DEFAULT_NAME); }
	public EnvisionList(String nameIn) { this(EnvisionDatatype.prim_var(), nameIn); }
	public EnvisionList(Primitives typeIn) { this(new EnvisionDatatype(typeIn), DEFAULT_NAME); }
	public EnvisionList(EnvisionDatatype typeIn) { this(typeIn, DEFAULT_NAME); }
	public EnvisionList(EnvisionDatatype typeIn, String nameIn) {
		super(new EnvisionDatatype(LIST), nameIn);
		type = typeIn;
		l = this;
	}
	
	public EnvisionList(Primitives typeIn, EArrayList listIn) {
		this(new EnvisionDatatype(typeIn), listIn);
	}
	
	public EnvisionList(EnvisionDatatype typeIn, EArrayList listIn) {
		super(new EnvisionDatatype(LIST));
		type = typeIn;
		list.addAll(listIn);
		l = this;
	}
	
	public EnvisionList(EnvisionList in) {
		super(new EnvisionDatatype(LIST));
		type = in.type;
		list.addAll(in.list);
		l = this;
	}
	
	public EnvisionList(EnvisionList in, EArrayList listIn) {
		super(new EnvisionDatatype(LIST));
		type = in.type;
		list.addAll(listIn);
		l = this;
	}
	
	@Override
	public EnvisionList copy() {
		//shallow copy
		return new EnvisionList(this);
	}
	
	//--------------------------------------
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		im(new InternalFunction(BOOLEAN, "add", VAR) { protected void body(Object[] a) { ret(l.add(cv(a[0]))); }});
		im(new InternalFunction(VAR, "addR", VAR) { protected void body(Object[] a) { ret(l.addR(cv(a[0]))); }});
		im(new InternalFunction(LIST, "addRT", VAR) { protected void body(Object[] a) { ret(l.addRT(cv(a[0]))); }});
		im(new InternalFunction(LIST, "clear") { protected void body(Object[] a) { ret(l.clear()); }});
		im(new InternalFunction(BOOLEAN, "contains", VAR) { protected void body(Object[] a) { ret(l.list.contains(cv(a[0]))); }});
		im(new InternalFunction(LIST, "copy") { protected void body(Object[] a) { ret(new EnvisionList(l)); }});
		im(new InternalFunction(LIST, "fill", VAR) { protected void body(Object[] a) { ret(l.fill(a)); }});
		im(new InternalFunction(LIST, "flip") { protected void body(Object[] a) { ret(l.flip()); }});
		im(new InternalFunction(VAR, "get", INT) { protected void body(Object[] a) { ret(l.get((long) cv(a[0]))); }});
		im(new InternalFunction(VAR, "getFirst") { protected void body(Object[] a) { ret(l.get(0)); }});
		im(new InternalFunction(VAR, "getLast") { protected void body(Object[] a) { ret(l.getLast()); }});
		im(new InternalFunction(STRING, "getListType") { protected void body(Object[] a) { ret(l.getListType()); }});
		im(new InternalFunction(BOOLEAN, "hasOne") { protected void body(Object[] a) { ret(l.size() == 1); }});
		im(new InternalFunction(BOOLEAN, "isEmpty") { protected void body(Object[] a) { ret(l.isEmpty()); }});
		im(new InternalFunction(BOOLEAN, "isNotEmpty") { protected void body(Object[] a) { ret(l.isNotEmpty()); }});
		im(new InternalFunction(BOOLEAN, "isSizeLocked") { protected void body(Object[] a) { ret(l.isSizeLocked()); }});
		im(new InternalFunction(LIST, "lockSize") { protected void body(Object[] a) { ret(l.setSizeLocked(true)); }});
		im(new InternalFunction(BOOLEAN, "notContains", VAR) { protected void body(Object[] a) { ret(l.list.notContains(cv(a[0]))); }});
		im(new InternalFunction(LIST, "push", VAR) { protected void body(Object[] a) { ret(l.push(cv(a[0]))); }});
		im(new InternalFunction(VAR, "pop") { protected void body(Object[] a) { ret(l.pop()); }});
		im(new InternalFunction(VAR, "remove", INT) { protected void body(Object[] a) { ret(l.remove((long) cv(a[0]))); }});
		im(new InternalFunction(VAR, "removeFirst") { protected void body(Object[] a) { ret(l.removeFirst()); }});
		im(new InternalFunction(VAR, "removeLast") { protected void body(Object[] a) { ret(l.removeLast()); }});
		im(new InternalFunction(VAR, "set", INT, VAR) { protected void body(Object[] a) { ret(l.set((long) cv(a[0]), cv(a[1]))); }});
		im(new InternalFunction(VAR, "setFirst", VAR) { protected void body(Object[] a) { ret(l.setFirst(cv(a[0]))); }});
		im(new InternalFunction(VAR, "setLast", VAR) { protected void body(Object[] a) { ret(l.setLast(cv(a[0]))); }});
		im(new InternalFunction(LIST, "setSize", INT) { protected void body(Object[] a) { ret(l.setSize(a)); }});
		im(new InternalFunction(LIST, "setSize", INT, VAR) { protected void body(Object[] a) { ret(l.setSize(a)); }});
		im(new InternalFunction(LIST, "shiftLeft", INT) { protected void body(Object[] a) { ret(l.shiftLeft(a)); }});
		im(new InternalFunction(LIST, "shiftRight", INT) { protected void body(Object[] a) { ret(l.shiftRight(a)); }});
		im(new InternalFunction(LIST, "shuffle") { protected void body(Object[] a) { ret(l.shuffle()); }});
		im(new InternalFunction(INT, "size") { protected void body(Object[] a) { ret(l.size()); }});
		im(new InternalFunction(LIST, "swap", INT, INT) { protected void body(Object[] a) { ret(l.swap((long) cv(a[0]), (long) cv(a[1]))); }});
		im(new InternalFunction(LIST, "unlockSize") { protected void body(Object[] a) { ret(l.setSizeLocked(false)); }});
		
		im(new InternalFunction(LIST, "concat", VAR_A) {
			protected void body(Object[] a) {
				for (Object obj : a) {
					if (obj instanceof EnvisionList) l.addAll((EnvisionList) obj);
					else l.add(obj);
				}
				ret(l);
			}
		});
	}
	
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
	
	public EnvisionList add(Object... val) { if (!sizeLocked) { list.addA(val); } else throw lockedError(); return this; }
	public EnvisionList addAll(List vals) { if (!sizeLocked) { list.addAll(vals); } else throw lockedError(); return this; }
	public EnvisionList addAll(EnvisionList listIn) { if (!sizeLocked) { list.addAll(listIn.list); } else throw lockedError(); return this; }
	public boolean add(Object o) { if (!sizeLocked) { return list.add(o); } else throw lockedError(); }
	public Object addR(Object o) { if (!sizeLocked) { return list.addR(o); } else throw lockedError(); }
	public EnvisionList addRT(Object o) { if (!sizeLocked) { list.addR(o); return this; } else throw lockedError(); }
	
	public long size() { return list.size(); }
	public boolean isEmpty() { return list.isEmpty(); }
	public boolean isNotEmpty() { return list.isNotEmpty(); }
	
	public Object get(long index) { return get((int) index); }
	public Object get(int index) { checkEmpty(); checkIndex(index); return list.get(index); }
	public Object getFirst() { checkEmpty(); return list.getFirst(); }
	public Object getLast() { checkEmpty(); return list.getLast(); }
	
	public Object remove(long index) { return remove((int) index); }
	public Object remove(int index) { checkEmpty(); checkIndex(index); if (!sizeLocked) { return list.remove(index); } else throw lockedError(); }
	public Object removeFirst() { checkEmpty(); if (!sizeLocked) { return list.removeFirst(); } else throw lockedError(); }
	public Object removeLast() { checkEmpty(); if (!sizeLocked) { return list.removeLast(); } else throw lockedError(); }
	
	public Object set(long index, Object obj) { return set((int) index, obj); }
	public Object set(int index, Object obj) { checkIndex(index); return list.set(index, obj); }
	public Object setFirst(Object obj) { checkEmpty(); return list.set(0, obj); }
	public Object setLast(Object obj) { checkEmpty(); return list.set(list.size() - 1, obj); }
	
	public EnvisionList flip() { return new EnvisionList(type, list.flip()); }
	
	public EnvisionList setSize(Object[] args) {
		if (args.length == 2) { return setSize((long) convert(args[0]), convert(args[1])); }
		if (args.length == 1) { return setSize((long) convert(args[0]), null); }
		return null;
	}
	
	public EnvisionList setSize(long size, Object defaultValue) {
		list.clear();
		for (int i = 0; i < size; i++) {
			list.add(defaultValue);
		}
		return this;
	}
	
	public EnvisionList shuffle() {
		EArrayList l = new EArrayList(list);
		Collections.shuffle(l);
		return new EnvisionList(this, l);
	}
	
	public EnvisionList setSizeLocked(boolean val) { sizeLocked = val; return this; }
	
	public EnvisionList swap(long indexA, long indexB) { return swap((int) indexA, (int) indexB); }
	public EnvisionList swap(int indexA, int indexB) { checkIndex(indexA); checkIndex(indexB); checkEmpty(); list.swap(indexA, indexB); return this; }
	
	public EnvisionList clear() {
		if (!sizeLocked) { list.clear(); }
		else { throw lockedError(); }
		return this;
	}
	
	public EnvisionList shiftLeft(Object[] args) {
		if (args.length == 0) { return new EnvisionList(this, list.shiftLeft(1)); }
		else if (args.length == 1) { return new EnvisionList(this, list.shiftLeft((int) (long) convert(args[0]))); }
		throw new RuntimeException("I am lazy");
	}
	
	public EnvisionList shiftRight(Object[] args) {
		if (args.length == 0) { return new EnvisionList(this, list.shiftRight(1)); }
		else if (args.length == 1) { return new EnvisionList(this, list.shiftRight((int) (long) convert(args[0]))); }
		throw new RuntimeException("I am lazy");
	}
	
	public EnvisionList fill(Object[] args) {
		System.out.println(args);
		return this;
	}
	
	public Object pop() {
		if (!sizeLocked) return list.pop();
		else throw lockedError();
	}
	
	public Object push(Object o) {
		if (!sizeLocked) {
			list.push(o);
			return this;
		}
		else throw lockedError();
	}
	
	public EnvisionDatatype getListType() { return type; }
	public EArrayList getEArrayList() { return list; }
	public boolean isSizeLocked() { return sizeLocked; }
	
	//---------------------------------------
	
	private void checkEmpty() {
		if (list.isEmpty()) { throw new EmptyListError(this); }
	}
	
	private void checkIndex(int index) {
		if (index < 0 || index >= list.size()) { throw new IndexOutOfBoundsError(index, this); }
	}
	
	private LockedListError lockedError() { return new LockedListError(this); }
	
	//---------------------------------------
	
	public static EnvisionList empty(String type) { return new EnvisionList(type); }
	
	public static EnvisionList of(Object[] vals) {
		EnvisionList list = new EnvisionList();
		list.add(vals);
		return list;
	}
	
	public static <E> EnvisionList of(String typeIn, E... vals) {
		EnvisionList list = new EnvisionList(typeIn);
		list.add(vals);
		return list;
	}
	
}
