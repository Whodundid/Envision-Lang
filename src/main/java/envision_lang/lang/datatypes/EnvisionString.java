package envision_lang.lang.datatypes;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.ArgLengthError;
import envision_lang.lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.lang.exceptions.errors.NoOverloadError;
import envision_lang.lang.exceptions.errors.NullVariableError;
import envision_lang.lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.lang.functions.FunctionPrototype;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;

/**
 * A script variable representing a list of characters.
 * 
 * @author Hunter Bragg
 */
public final class EnvisionString extends EnvisionVariable<String> {
	
	public static final IDatatype STRING_TYPE = EnvisionStaticTypes.STRING_TYPE;
	
	public static final EnvisionString EMPTY_STRING = EnvisionStringClass.newString();
	
	/**
	 * The internal StringBuilder for which all strings are built from.
	 * Instead of directly using a string, a StringBuilder is more memory
	 * efficient when dealing with string concatenation and appending.
	 */
	public final StringBuilder string_val;
	
	//--------------
	// Constructors
	//--------------
	
	EnvisionString() {
		super(EnvisionStringClass.STRING_CLASS);
		// leave 'string_val' null
		string_val = null;
	}
	
	EnvisionString(String valueIn) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = new StringBuilder(valueIn);
	}
	
	EnvisionString(EnvisionString in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = in.string_val;
	}
	
	EnvisionString(EnvisionObject in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = new StringBuilder(String.valueOf(in));
	}
	
	EnvisionString(StringBuilder in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = in;
	}
	
	EnvisionString(Object in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = new StringBuilder(String.valueOf(in));
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EnvisionString)) return false;
		//because 'string_val' is a StringBuilder, must convert to string type first
		return ((EnvisionString) obj).toString().equals(toString());
	}
	
	@Override
	public String toString() {
		if (string_val == null) return "";
		return string_val.toString();
	}
	
	/**
	 * Return this exact same String.
	 */
	@Override
	public EnvisionString copy() {
		return this;
	}
	
	@Override
	public String convertToJavaObject() {
		if (string_val == null) return "";
		return string_val.toString();
	}
	
	@Override
	public EnvisionString get() {
		return this;
	}
	
	@Override
	public String get_i() {
		if (string_val == null) return "";
		return string_val.toString();
	}
	
	@Override
	public boolean supportsOperator(Operator op) {
		return switch (op) {
		case ADD, MUL -> true;
		case ADD_ASSIGN, MUL_ASSIGN -> true;
		default -> false;
		};
	}
	
	@Override
	@Broken(reason="Strings should be immutable and lines 157 and 183 breaks this entirely!")
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//reject null object values
		if (obj == null) throw new NullVariableError();
		
		//only support '+', '+=', '*', '*='
		
		// extract this variable's direct scope entry so that assignment operations can effectively update the value
		ScopeEntry scopeEntry = interpreter.scope().getTyped(scopeName);
		
		//addition operators
		if (op == Operator.ADD || op == Operator.ADD_ASSIGN) {
			String obj_toString = null;
			
			//convert incoming object to a string representation
			if (obj instanceof EnvisionVariable env_var) 	obj_toString = env_var.toString();
			else if (obj instanceof EnvisionList list)		obj_toString = EnvisionStringFormatter.formatPrint(interpreter, list, true);
			else if (obj instanceof ClassInstance inst) 	obj_toString = EnvisionStringFormatter.formatPrint(interpreter, inst, true);
			else 											obj_toString = obj.toString();
			
			//add operator
			if (op == Operator.ADD) {
				if (string_val == null) return EnvisionStringClass.valueOf(obj_toString);
				return EnvisionStringClass.valueOf(string_val.append(obj_toString));
			}
			//add assign operator
			else {
				string_val.append(obj_toString);
				return this;
			}
		}
		
		//mul_addition operators
		if (op == Operator.MUL || op == Operator.MUL_ASSIGN) {
			long multiply_val = 0;
			
			//convert incoming object to an integer representation
			if (obj instanceof EnvisionInt env_int) multiply_val = env_int.int_val;
			else throw new InvalidDatatypeError(EnvisionStaticTypes.INT_TYPE, obj.getDatatype());
			
			//repeat current string 'x' number of times
			StringBuilder new_val = null;
			
			if (string_val == null) new_val = null;
			else if (string_val.isEmpty()) new_val = null;
			else new_val = new StringBuilder();
			
			for (int i = 0; i < multiply_val; i++) {
				if (string_val.isEmpty()) break;
				new_val.append(string_val);
			}
			
			//mul operator
			if (op == Operator.MUL) return EnvisionStringClass.valueOf(new_val);
			//mul assign operator
			else return scopeEntry.setR(EnvisionStringClass.valueOf(new_val));
		}
		
		return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
	}
	
	@Override
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
		
		return switch (funcName) {
		case "equals" -> EnvisionBooleanClass.valueOf(equals(args[0]));
		case "toString" -> EnvisionStringClass.valueOf(string_val);
		case "toList" -> toList();
		case "length" -> length();
		case "isEmpty" -> isEmpty();
		case "isNotEmpty" -> isNotEmpty();
		case "isChar" -> isChar();
		case "get" -> get();
		case "reverse" -> reverse();
		case "toUpperCase" -> toUpperCase();
		case "toLowerCase" -> toLowerCase();
		case "substring" -> substring(args);
		case "compareTo" -> compareTo((EnvisionString) args[0]);
		default -> super.handlePrimitive(proto, args);
		};
	}
	
	//---------
	// Methods
	//---------

	public EnvisionChar charAt(EnvisionInt pos) { return charAt((int) pos.int_val); }
	public EnvisionChar charAt(long pos) { return charAt((int) pos); }
	public EnvisionChar charAt(int pos) { return EnvisionCharClass.valueOf(string_val.charAt(pos)); }
	
	public char charAt_i(long pos) {
		if (string_val == null) return "".charAt((int) pos);
		return string_val.charAt((int) pos);
	}
	
	public char charAt_i(int pos) {
		if (string_val == null) return "".charAt(pos);
		return string_val.charAt(pos);
	}
	
	public EnvisionList toList() {
		if (string_val == null) return EnvisionListClass.newList();
		
		EnvisionList list = EnvisionListClass.newList(EnvisionStaticTypes.CHAR_TYPE);
		for (int i = 0; i < string_val.length(); i++) {
			EnvisionChar c = EnvisionCharClass.valueOf(string_val.charAt(i));
			list.add(c);
		}
		return list;
	}
	
	public EList<EnvisionObject> toList_i() {
		if (string_val == null) return new EArrayList<>();
		return toList().getInternalList();
	}
	
	public EnvisionBoolean contains(String in) { return EnvisionBooleanClass.valueOf(contains_i(in)); }
	public boolean contains_i(String in) {
		if (string_val == null) return false;
		return string_val.toString().contains(in);
	}	
	
	public EnvisionBoolean matches(String in) { return EnvisionBooleanClass.valueOf(matches_i(in)); }
	public boolean matches_i(String in) {
		if (string_val == null) return "".equals(in);
		return string_val.toString().matches(in);
	}	
	
	public EnvisionInt length() { return EnvisionIntClass.valueOf(length_i()); }
	public int length_i() {
		if (string_val == null) return 0;
		return string_val.length();
	}	
	
	public EnvisionBoolean isEmpty() { return EnvisionBooleanClass.valueOf(isEmpty_i()); }
	public boolean isEmpty_i() {
		if (string_val == null) return true;
		return string_val.isEmpty();
	}	
	
	public EnvisionBoolean isNotEmpty() { return EnvisionBooleanClass.valueOf(isNotEmpty_i()); }
	public boolean isNotEmpty_i() {
		if (string_val == null) return false;
		return !string_val.isEmpty();
	}	
	
	public EnvisionBoolean isChar() { return EnvisionBooleanClass.valueOf(isChar_i()); }
	public boolean isChar_i() {
		if (string_val == null) return false;
		return string_val.length() == 1;
	}	
	
	public EnvisionInt ascii() throws EnvisionLangError { return EnvisionIntClass.valueOf(ascii_i()); }
	public int ascii_i() throws EnvisionLangError {
		if (!isChar_i()) throw new EnvisionLangError("Given string is not a char! Length must be 1!");
		return string_val.charAt(0);
	}	
	
	public EnvisionString toLowerCase() { return EnvisionStringClass.valueOf(toLowerCase_i()); }
	public String toLowerCase_i() {
		if (string_val == null) return "";
		return string_val.toString().toLowerCase();
	}
	
	public EnvisionString toUpperCase() { return EnvisionStringClass.valueOf(toUpperCase_i()); }
	public String toUpperCase_i() {
		if (string_val == null) return "";
		return string_val.toString().toUpperCase();
	}
	
	public String substring_i(int start) { return substring_i(start, length_i()); }
	public String substring_i(int start, int end) {
		if (string_val == null) return "".substring(start, end);
		return string_val.substring(start, end);
	}
	
	public EnvisionString substring(EnvisionObject[] args) {
		if (args.length == 1) return substring((EnvisionInt) args[0]);
		else if (args.length == 2) return substring((EnvisionInt) args[0], (EnvisionInt) args[1]);
		throw new ArgLengthError("substring", 2, args.length);
	}
	
	public EnvisionString substring(EnvisionInt start) {
		return substring((int) start.int_val);
	}
	
	public EnvisionString substring(EnvisionInt start, EnvisionInt end) {
		return substring((int) start.int_val, (int) end.int_val);
	}
	
	public EnvisionString substring(int start) {
		return EnvisionStringClass.valueOf(substring_i(start));
	}
	
	public EnvisionString substring(int start, int end) {
		return EnvisionStringClass.valueOf(substring_i(start, end));
	}
	
	public EnvisionString reverse() {
		if (string_val == null) return EnvisionString.EMPTY_STRING;
		return EnvisionStringClass.valueOf(new StringBuilder(string_val).reverse());
	}
	
	public EnvisionInt compareTo(EnvisionString str) {
		if (string_val == null) return EnvisionIntClass.valueOf("".compareTo(str.toString()));
		return EnvisionIntClass.valueOf(string_val.compareTo(str.string_val));
	}
	
}