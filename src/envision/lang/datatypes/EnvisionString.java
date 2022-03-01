package envision.lang.datatypes;

import static envision.lang.util.Primitives.*;

import envision.exceptions.EnvisionError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionList;
import envision.lang.util.InternalMethod;
import envision.lang.util.Primitives;
import eutil.datatypes.EArrayList;

/** A script variable representing a list of characters. */
public class EnvisionString extends EnvisionVariable {
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionString() { this(DEFAULT_NAME, ""); }
	public EnvisionString(String valueIn) { this(DEFAULT_NAME, valueIn); }
	public EnvisionString(String nameIn, String valueIn) {
		super(Primitives.STRING.toDatatype(), nameIn);
		var_value = valueIn;
	}
	
	public EnvisionString(EnvisionString in) {
		super(Primitives.STRING.toDatatype(), in.name);
		var_value = in.var_value;
	}
	
	public EnvisionString(EnvisionObject in) {
		super(Primitives.STRING.toDatatype(), in.getName());
		var_value = String.valueOf(in);
	}
	
	public EnvisionString(Object in) {
		super(Primitives.STRING.toDatatype(), DEFAULT_NAME);
		var_value = String.valueOf(in);
	}
	
	//-----------
	// Overrides
	//-----------
	
	//@Override
	//public String toString() {
	//	return "\"" + var_value + "\"";
	//}
	
	@Override
	public EnvisionString copy() {
		return new EnvisionString(this);
	}
	
	/*
	@Override
	public void runInternalMethod(String methodName, EnvisionInterpreter interpreter, EArrayList args) {
		switch (methodName) {
		case "charAt": ret(charAt(Integer.valueOf((String) args.get(0))));
		case "toList": ret(toList());
		//case "contains": returnValue(contains(args.get(0)));
		//case "matches": return new StorageBox(matches(String.valueOf(args.get(0))), EDataType.BOOLEAN);
		case "len": ret(length());
		case "isEmpty": ret(isEmpty());
		case "isNotEmpty": ret(isNotEmpty());
		case "isChar": ret(isChar());
		case "ascii": ret(ascii());
		case "lower": ret(toLowerCase());
		case "upper": ret(toUpperCase());
		//case "sub": return new StorageBox(subString(args), EDataType.STRING);
		case "get": ret(this);
		case "set": ret(set(args.get(0)));
		default:
			super.runInternalMethod(methodName, interpreter, args);
		}
	}
	*/
	
	@Override
	protected void registerInternalMethods() {
		super.registerInternalMethods();
		im(new InternalMethod(CHAR, "charAt", INT) { protected void body(Object[] a) { ret(charAt((long) a[0])); }});
		im(new InternalMethod(LIST, "toList") { protected void body(Object[] a) { ret(toList()); }});
		im(new InternalMethod(INT, "length") { protected void body(Object[] a) { ret(length()); }});
		im(new InternalMethod(BOOLEAN, "isEmpty") { protected void body(Object[] a) { ret(isEmpty()); }});
		im(new InternalMethod(BOOLEAN, "isNotEmtpy") { protected void body(Object[] a) { ret(isNotEmpty()); }});
		im(new InternalMethod(BOOLEAN, "isChar") { protected void body(Object[] a) { ret(isChar()); }});
		im(new InternalMethod(INT, "ascii") { protected void body(Object[] a) { ret(ascii()); }});
		im(new InternalMethod(STRING, "lower") { protected void body(Object[] a) { ret(toLowerCase()); }});
		im(new InternalMethod(STRING, "upper") { protected void body(Object[] a) { ret(toUpperCase()); }});
		im(new InternalMethod(STRING, "valueOf", VAR) { protected void body(Object[] a) { ret(of(a[0])); }});
		im(new InternalMethod(STRING, "get") { protected void body(Object[] a) { ret(get()); }});
		im(new InternalMethod(STRING, "set", VAR) { protected void body(Object[] a) { ret(set(String.valueOf(a[0]))); }});
		im(new InternalMethod(STRING, "subString", INT) { protected void body(Object[] a) { ret(((String) get()).substring((int) (long) a[0])); }});
		im(new InternalMethod(STRING, "subString", INT, INT) { protected void body(Object[] a) { ret(((String) get()).substring((int) (long) a[0], (int) (long) a[1])); }});
		im(new InternalMethod(INT, "cmp", STRING) { protected void body(Object[] a) { ret(((String) get()).compareTo((String) cv(a[0]))); }});
	}
	
	@Override
	protected EnvisionObject runConstructor(EnvisionInterpreter interpreter, Object[] args) {
		if (args.length == 0) { return new EnvisionString(); }
		if (args.length == 1) {
			Object obj = args[0];
			
			if (obj instanceof EnvisionObject) { return new EnvisionString((EnvisionObject) obj); }
			
			return new EnvisionString(obj);
		}
		return null;
	}
	
	@Override
	public boolean equals(Object in) {
		String s = (String) get();
		return s.equals(in);
	}
	
	//@Override public EnvisionString base() { return new EnvisionString(); }
	
	//---------
	// Methods
	//---------
	
	public char charAt(long pos) { return charAt((int) pos); }
	public char charAt(int pos) {
		return ((String) get()).charAt(pos);
	}
	
	public EnvisionList toList() {
		EnvisionList list = new EnvisionList(Primitives.CHAR);
		String s = (String) get();
		for (int i = 0; i < s.length(); i++) {
			list.add(s.charAt(i) + "");
		}
		return list;
	}
	
	public boolean contains(String in) {
		String s = (String) get();
		return s.contains(in);
	}
	
	public boolean matches(String in) {
		String s = (String) get();
		return s.matches(in);
	}
	
	public int length() {
		String s = (String) get();
		return s.length();
	}
	
	public boolean isEmpty() {
		String s = (String) get();
		return s.isEmpty();
	}
	
	public boolean isNotEmpty() {
		String s = (String) get();
		return s.length() > 0;
	}
	
	public boolean isChar() {
		String s = (String) get();
		return s.length() == 1;
	}
	
	public int ascii() throws EnvisionError {
		if (!isChar()) { throw new EnvisionError("Given string is not a char! Length must be 1!"); }
		String s = (String) get();
		return s.charAt(0);
	}
	
	public String toLowerCase() {
		String s = (String) get();
		return s.toLowerCase();
	}
	
	public String toUpperCase() {
		String s = (String) get();
		return s.toUpperCase();
	}
	
	public String subString(EArrayList<String> args) {
		String s = (String) get();
		
		if (args.size() == 1) {
			try {
				int start = Integer.valueOf(args.get(0));
				
				return s.substring(start);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		else if (args.size() == 2) {
			try {
				int start = Integer.valueOf(args.get(0));
				int end = Integer.valueOf(args.get(1));
				
				return s.substring(start, end);
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		return null;
	}
	
	//--------
	// Static
	//--------
	
	public static EnvisionString of(String val) { return new EnvisionString(val); }
	public static EnvisionString of(Object val) { return new EnvisionString(String.valueOf(val)); }
	
}
