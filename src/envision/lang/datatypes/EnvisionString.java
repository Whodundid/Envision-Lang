package envision.lang.datatypes;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.lang.EnvisionObject;
import envision.lang.util.Primitives;

/** A script variable representing a list of characters. */
public class EnvisionString extends EnvisionVariable {
	
	public String string_val;
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionString() { this(""); }
	protected EnvisionString(String valueIn) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = valueIn;
	}
	
	protected EnvisionString(EnvisionString in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = in.string_val;
	}
	
	protected EnvisionString(EnvisionObject in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = String.valueOf(in);
	}
	
	protected EnvisionString(Object in) {
		super(EnvisionStringClass.STRING_CLASS);
		string_val = String.valueOf(in);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionObject get() {
		return this;
	}
	
	@Override
	public Object get_i() {
		return string_val;
	}
	
	@Override
	public EnvisionVariable set(EnvisionObject valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof EnvisionString env_str) {
			string_val = env_str.string_val;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-string value to a string!");
	}
	
	@Override
	public EnvisionVariable set_i(Object valIn) throws FinalVarReassignmentError {
		if (isFinal()) throw new FinalVarReassignmentError(this, valIn);
		if (valIn instanceof String str) {
			string_val = str;
			return this;
		}
		throw new EnvisionError("Attempted to internally set non-string value to a string!");
	}
	
	@Override
	public String toString() {
		return "\"" + string_val + "\"";
	}
	
	@Override
	public EnvisionString copy() {
		return new EnvisionString(this);
	}
	
	//---------
	// Methods
	//---------
	
	public char charAt_i(long pos) { return string_val.charAt((int) pos); }
	public char charAt_i(int pos) { return string_val.charAt(pos); }
	public EnvisionChar charAt(long pos) { return charAt((int) pos); }
	public EnvisionChar charAt(int pos) {
		EnvisionChar c = EnvisionCharClass.newChar(string_val.charAt(pos));
		return c;
	}
	
	public EnvisionList toList() {
		EnvisionList list = EnvisionListClass.newList(Primitives.CHAR);
		for (int i = 0; i < string_val.length(); i++) {
			EnvisionChar c = EnvisionCharClass.newChar(string_val.charAt(i));
			list.add(c);
		}
		return list;
	}
	
	public boolean contains_i(String in) { return string_val.contains(in); }
	public EnvisionBoolean contains(String in) {
		return EnvisionBooleanClass.newBoolean(contains_i(in));
	}
	
	public boolean matches_i(String in) { return string_val.matches(in); }
	public EnvisionBoolean matches(String in) {
		return EnvisionBooleanClass.newBoolean(matches_i(in));
	}
	
	public int length_i() { return string_val.length(); }
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
	
	public int ascii_i() throws EnvisionError {
		if (!isChar_i()) throw new EnvisionError("Given string is not a char! Length must be 1!");
		return string_val.charAt(0);
	}
	
	public EnvisionInt ascii() throws EnvisionError {
		if (!isChar_i()) throw new EnvisionError("Given string is not a char! Length must be 1!");
		return EnvisionIntClass.newInt((int) string_val.charAt(0));
	}
	
	public String toLowerCase_i() { return string_val.toLowerCase(); }
	public EnvisionString toLowerCase() {
		return EnvisionStringClass.newString(toLowerCase_i());
	}
	
	public String toUpperCase_i() { return string_val.toUpperCase(); }
	public EnvisionString toUpperCase() {
		return EnvisionStringClass.newString(toUpperCase_i());
	}
	
	public String substring_i(int start) { return substring_i(start, string_val.length()); }
	public String substring_i(int start, int end) { return string_val.substring(start, end); }
	public EnvisionString substring(EnvisionInt start) { return substring((int) start.long_val); }
	public EnvisionString substring(EnvisionInt start, EnvisionInt end) { return substring((int) start.long_val, (int) end.long_val); }
	public EnvisionString substring(int start) { return EnvisionStringClass.newString(substring_i(start)); }
	public EnvisionString substring(int start, int end) {
		return EnvisionStringClass.newString(substring_i(start, end));
	}
	
	public EnvisionInt compareTo(EnvisionString str) {
		return EnvisionIntClass.newInt(string_val.compareTo(str.string_val));
	}
	
}
