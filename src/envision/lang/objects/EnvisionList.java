package envision.lang.objects;

import static envision.lang.util.EnvisionDataType.*;

import envision.exceptions.errors.listErrors.EmptyListError;
import envision.exceptions.errors.listErrors.IndexOutOfBoundsError;
import envision.exceptions.errors.listErrors.LockedListError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.InternalMethod;
import envision.lang.variables.EnvisionInt;
import eutil.datatypes.EArrayList;
import eutil.strings.StringUtil;
import java.util.Collections;
import java.util.List;

public class EnvisionList extends EnvisionObject {

	private final EArrayList list = new EArrayList();
	private final String type;
	private boolean sizeLocked = false;
	private EnvisionList l;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionList() { this(OBJECT, "noname"); }
	public EnvisionList(String nameIn) { this(OBJECT, nameIn); }
	public EnvisionList(EnvisionDataType typeIn) { this(typeIn.type, "noname"); }
	public EnvisionList(EnvisionDataType typeIn, String nameIn) { this(typeIn.type, nameIn); }
	public EnvisionList(String typeIn, String nameIn) {
		super(LIST, nameIn);
		type = typeIn;
		l = this;
	}
	
	public EnvisionList(EnvisionDataType typeIn, EArrayList listIn) { this(typeIn.type, listIn); }
	public EnvisionList(String typeIn, EArrayList listIn) {
		super(LIST);
		type = typeIn;
		list.addAll(listIn);
		l = this;
	}
	
	public EnvisionList(EnvisionList in) {
		super(LIST);
		type = in.type;
		list.addAll(in.list);
		l = this;
	}
	
	public EnvisionList(EnvisionList in, EArrayList listIn) {
		super(LIST);
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
		im(new InternalMethod(BOOLEAN, "add", OBJECT) { protected void body(Object[] a) { ret(l.add(cv(a[0]))); }});
		im(new InternalMethod(OBJECT, "addR", OBJECT) { protected void body(Object[] a) { ret(l.addR(cv(a[0]))); }});
		im(new InternalMethod(LIST, "addRT", OBJECT) { protected void body(Object[] a) { ret(l.addRT(cv(a[0]))); }});
		im(new InternalMethod(LIST, "clear") { protected void body(Object[] a) { ret(l.clear()); }});
		im(new InternalMethod(BOOLEAN, "contains", OBJECT) { protected void body(Object[] a) { ret(l.list.contains(cv(a[0]))); }});
		im(new InternalMethod(LIST, "copy") { protected void body(Object[] a) { ret(new EnvisionList(l)); }});
		im(new InternalMethod(LIST, "fill", OBJECT) { protected void body(Object[] a) { ret(l.fill(a)); }});
		im(new InternalMethod(LIST, "flip") { protected void body(Object[] a) { ret(l.flip()); }});
		im(new InternalMethod(OBJECT, "get", INT) { protected void body(Object[] a) { ret(l.get((long) cv(a[0]))); }});
		im(new InternalMethod(OBJECT, "getFirst") { protected void body(Object[] a) { ret(l.get(0)); }});
		im(new InternalMethod(OBJECT, "getLast") { protected void body(Object[] a) { ret(l.getLast()); }});
		im(new InternalMethod(STRING, "getListType") { protected void body(Object[] a) { ret(l.getListType()); }});
		im(new InternalMethod(BOOLEAN, "hasOne") { protected void body(Object[] a) { ret(l.size() == 1); }});
		im(new InternalMethod(BOOLEAN, "isEmpty") { protected void body(Object[] a) { ret(l.isEmpty()); }});
		im(new InternalMethod(BOOLEAN, "isNotEmpty") { protected void body(Object[] a) { ret(l.isNotEmpty()); }});
		im(new InternalMethod(BOOLEAN, "isSizeLocked") { protected void body(Object[] a) { ret(l.isSizeLocked()); }});
		im(new InternalMethod(LIST, "lockSize") { protected void body(Object[] a) { ret(l.setSizeLocked(true)); }});
		im(new InternalMethod(BOOLEAN, "notContains", OBJECT) { protected void body(Object[] a) { ret(l.list.notContains(cv(a[0]))); }});
		im(new InternalMethod(LIST, "push", OBJECT) { protected void body(Object[] a) { ret(l.push(cv(a[0]))); }});
		im(new InternalMethod(OBJECT, "pop") { protected void body(Object[] a) { ret(l.pop()); }});
		im(new InternalMethod(OBJECT, "remove", INT) { protected void body(Object[] a) { ret(l.remove((long) cv(a[0]))); }});
		im(new InternalMethod(OBJECT, "removeFirst") { protected void body(Object[] a) { ret(l.removeFirst()); }});
		im(new InternalMethod(OBJECT, "removeLast") { protected void body(Object[] a) { ret(l.removeLast()); }});
		im(new InternalMethod(OBJECT, "set", INT, OBJECT) { protected void body(Object[] a) { ret(l.set((long) cv(a[0]), cv(a[1]))); }});
		im(new InternalMethod(OBJECT, "setFirst", OBJECT) { protected void body(Object[] a) { ret(l.setFirst(cv(a[0]))); }});
		im(new InternalMethod(OBJECT, "setLast", OBJECT) { protected void body(Object[] a) { ret(l.setLast(cv(a[0]))); }});
		im(new InternalMethod(LIST, "setSize", INT) { protected void body(Object[] a) { ret(l.setSize(a)); }});
		im(new InternalMethod(LIST, "setSize", INT, OBJECT) { protected void body(Object[] a) { ret(l.setSize(a)); }});
		im(new InternalMethod(LIST, "shiftLeft", INT) { protected void body(Object[] a) { ret(l.shiftLeft(a)); }});
		im(new InternalMethod(LIST, "shiftRight", INT) { protected void body(Object[] a) { ret(l.shiftRight(a)); }});
		im(new InternalMethod(LIST, "shuffle") { protected void body(Object[] a) { ret(l.shuffle()); }});
		im(new InternalMethod(INT, "size") { protected void body(Object[] a) { ret(l.size()); }});
		im(new InternalMethod(LIST, "swap", INT, INT) { protected void body(Object[] a) { ret(l.swap((long) cv(a[0]), (long) cv(a[1]))); }});
		im(new InternalMethod(LIST, "unlockSize") { protected void body(Object[] a) { ret(l.setSizeLocked(false)); }});
		
		im(new InternalMethod(LIST, "concat", OBJECT_A) {
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
	
	public Object pop() { if (!sizeLocked) { return list.pop(); } else throw lockedError(); }
	public Object push(Object o) { if (!sizeLocked) { list.push(o); return this; } else throw lockedError(); }
	
	public String getListType() { return type; }
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
	
	public static EnvisionList of(Object... vals) {
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
