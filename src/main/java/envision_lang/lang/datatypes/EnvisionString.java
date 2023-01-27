package envision_lang.lang.datatypes;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.ArgLengthError;
import envision_lang.exceptions.errors.FinalVarReassignmentError;
import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.exceptions.errors.NoOverloadError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.internal.FunctionPrototype;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.util.EList;

/**
 * A script variable representing a list of characters.
 * 
 * @author Hunter Bragg
 */
public class EnvisionString extends EnvisionVariable {
	
	/**
	 * The internal StringBuilder for which all strings are built from.
	 * Instead of directly using a string, a StringBuilder is more memory
	 * efficient when dealing with string concatenation and appending.
	 */
	protected StringBuilder string_val;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionString() { this(""); }
	protected EnvisionString(String valueIn) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = new StringBuilder(valueIn);
	}
	
	protected EnvisionString(EnvisionString in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = in.string_val;
	}
	
	protected EnvisionString(EnvisionObject in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = new StringBuilder(String.valueOf(in));
	}
	
	protected EnvisionString(StringBuilder in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = in;
	}
	
	protected EnvisionString(Object in) {
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
		return string_val.toString();
	}
	
	@Override
	public EnvisionString copy() {
		return EnvisionStringClass.newString(this);
	}
	
	@Override
	public String convertToJavaObject() {
		return string_val.toString();
	}
	
	@Override
	public EnvisionString get() {
		return this;
	}
	
	@Override
	public String get_i() {
		return string_val.toString();
	}
	
	@Override
	public EnvisionVariable set(EnvisionObject valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof EnvisionString env_str) {
			string_val = env_str.string_val;
			return this;
		}
		throw new EnvisionLangError("Attempted to internally set non-string value to a string!");
	}
	
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof String str) {
			string_val = new StringBuilder(str);
			return this;
		}
		throw new EnvisionLangError("Attempted to internally set non-string value to a string!");
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
	public EnvisionObject handleOperatorOverloads
		(EnvisionInterpreter interpreter, String scopeName, Operator op, EnvisionObject obj)
			throws UnsupportedOverloadError
	{
		//reject null object values
		if (obj == null) throw new NullVariableError();
		
		//only support '+', '+=', '*', '*='
		
		//addition operators
		if (op == Operator.ADD || op == Operator.ADD_ASSIGN) {
			String obj_toString = null;
			
			//convert incoming object to a string representation
			if (obj instanceof EnvisionVariable env_var) 	obj_toString = env_var.toString();
			else if (obj instanceof EnvisionList list)		obj_toString = EnvisionStringFormatter.formatPrint(interpreter, list, true);
			else if (obj instanceof ClassInstance inst) 	obj_toString = EnvisionStringFormatter.formatPrint(interpreter, inst, true);
			else 											obj_toString = obj.toString();
			
			//add operator
			if (op == Operator.ADD) return EnvisionStringClass.newString(string_val.append(obj_toString));
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
			else throw new InvalidDatatypeError(StaticTypes.INT_TYPE, obj.getDatatype());
			
			//repeat current string 'x' number of times
			StringBuilder new_val = new StringBuilder();
			for (int i = 0; i < multiply_val; i++) new_val.append(string_val);
			
			//mul operator
			if (op == Operator.MUL) return EnvisionStringClass.newString(new_val.toString());
			//mul assign operator
			else {
				string_val = new_val;
				return this;
			}
		}
			
		//throw error if this point is reached
		//throw new UnsupportedOverloadError(this, op, "[" + obj.getDatatype() + ":" + obj + "]");
		return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
	}
	
	@Override
	protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
		String funcName = proto.getFunctionName();
		if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
		
		return switch (funcName) {
		case "equals" -> EnvisionBooleanClass.newBoolean(equals(args[0]));
		case "toString" -> EnvisionStringClass.newString(string_val);
		case "toList" -> toList();
		case "length" -> length();
		case "isEmpty" -> isEmpty();
		case "isNotEmpty" -> isNotEmpty();
		case "isChar" -> isChar();
		case "get" -> get();
		case "set" -> set(args[0]);
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
	
	public char charAt_i(long pos) { return string_val.charAt((int) pos); }
	public char charAt_i(int pos) { return string_val.charAt(pos); }
	public EnvisionChar charAt(EnvisionInt pos) { return charAt((int) pos.int_val); }
	public EnvisionChar charAt(long pos) { return charAt((int) pos); }
	public EnvisionChar charAt(int pos) {
		EnvisionChar c = EnvisionCharClass.newChar(string_val.charAt(pos));
		return c;
	}
	
	public EList<EnvisionObject> toList_i() { return toList().getInternalList(); }
	public EnvisionList toList() {
		EnvisionList list = EnvisionListClass.newList(Primitives.CHAR);
		for (int i = 0; i < string_val.length(); i++) {
			EnvisionChar c = EnvisionCharClass.newChar(string_val.charAt(i));
			list.add(c);
		}
		return list;
	}
	
	public boolean contains_i(String in) { return string_val.toString().contains(in); }
	public EnvisionBoolean contains(String in) {
		return EnvisionBooleanClass.newBoolean(contains_i(in));
	}
	
	public boolean matches_i(String in) { return string_val.toString().matches(in); }
	public EnvisionBoolean matches(String in) {
		return EnvisionBooleanClass.newBoolean(matches_i(in));
	}
	
	public long length_i() { return string_val.length(); }
	public EnvisionInt length() {
		return EnvisionIntClass.newInt(length_i());
	}
	
	public boolean isEmpty_i() { return string_val.isEmpty(); }
	public EnvisionBoolean isEmpty() {
		return EnvisionBooleanClass.newBoolean(isEmpty_i());
	}
	
	public boolean isNotEmpty_i() { return !string_val.isEmpty(); }
	public EnvisionBoolean isNotEmpty() {
		return EnvisionBooleanClass.newBoolean(isNotEmpty_i());
	}
	
	public boolean isChar_i() { return string_val.length() == 1; }
	public EnvisionBoolean isChar() {
		return EnvisionBooleanClass.newBoolean(isChar_i());
	}
	
	public int ascii_i() throws EnvisionLangError {
		if (!isChar_i()) throw new EnvisionLangError("Given string is not a char! Length must be 1!");
		return string_val.charAt(0);
	}
	
	public EnvisionInt ascii() throws EnvisionLangError {
		if (!isChar_i()) throw new EnvisionLangError("Given string is not a char! Length must be 1!");
		return EnvisionIntClass.newInt((int) string_val.charAt(0));
	}
	
	public String toLowerCase_i() { return string_val.toString().toLowerCase(); }
	public EnvisionString toLowerCase() {
		return EnvisionStringClass.newString(toLowerCase_i());
	}
	
	public String toUpperCase_i() { return string_val.toString().toUpperCase(); }
	public EnvisionString toUpperCase() {
		return EnvisionStringClass.newString(toUpperCase_i());
	}
	
	public String substring_i(int start) { return substring_i(start, string_val.length()); }
	public String substring_i(int start, int end) { return string_val.substring(start, end); }
	
	public EnvisionString substring(EnvisionObject[] args) {
		if (args.length == 1) return substring((EnvisionInt) args[0]);
		else if (args.length == 2) return substring((EnvisionInt) args[0], (EnvisionInt) args[1]);
		throw new ArgLengthError("substring", 2, args.length);
	}
	public EnvisionString substring(EnvisionInt start) { return substring((int) start.int_val); }
	public EnvisionString substring(EnvisionInt start, EnvisionInt end) { return substring((int) start.int_val, (int) end.int_val); }
	public EnvisionString substring(int start) { return EnvisionStringClass.newString(substring_i(start)); }
	public EnvisionString substring(int start, int end) {
		return EnvisionStringClass.newString(substring_i(start, end));
	}
	
	public EnvisionString reverse() {
		return EnvisionStringClass.newString(new StringBuilder(string_val).reverse());
	}
	
	public EnvisionInt compareTo(EnvisionString str) {
		return EnvisionIntClass.newInt(string_val.compareTo(str.string_val));
	}
	
}