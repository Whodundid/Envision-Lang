package envision.lang.datatypes;

import static envision.lang.util.Primitives.*;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.ArgLengthError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.internal.EnvisionNull;
import envision.lang.util.InstanceFunction;
import envision.lang.util.Primitives;

public class EnvisionStringClass extends EnvisionClass {

	/**
	 * The singular, static String class for which all Envision:String
	 * objects are derived from.
	 */
	public static final EnvisionStringClass STRING_CLASS = new EnvisionStringClass();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionStringClass() {
		super(Primitives.STRING);
		
		//define static class members
		staticClassScope.defineFunction(new IFunc_static_valueOf());
		
		//set final to prevent user-extension
		setFinal();
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionString newString() { return newString(""); }
	public static EnvisionString newString(Object value) { return newString(String.valueOf(value)); }
	public static EnvisionString newString(String value) {
		EnvisionString str = new EnvisionString(value);
		STRING_CLASS.defineScopeMembers(str);
		return str;
	}
	
	public static EnvisionString newString(StringBuilder value) {
		EnvisionString str = new EnvisionString(value);
		STRING_CLASS.defineScopeMembers(str);
		return str;
	}
	
	public static EnvisionString newStrting(EnvisionObject value) {
		EnvisionString str = new EnvisionString(value);
		STRING_CLASS.defineScopeMembers(str);
		return str;
	}
	
	/**
	 * Takes in some EnvisionObject and returns it's direct String
	 * representation. Accounts for null values. Does not account for
	 * class instance 'toString' overrides however.
	 * 
	 * @param value The object to be converted to a string
	 * @return An EnvisionString containing the String value of the given
	 *         object
	 */
	public static EnvisionString valueOf(EnvisionObject value) {
		String str_val = null;
		if (value == null) str_val = EnvisionNull.NULL.getTypeString();
		else str_val = value.getTypeString();
		EnvisionString str = new EnvisionString(str_val);
		STRING_CLASS.defineScopeMembers(str);
		return str;
	}
	
	/**
	 * Joins the values of each EnvisionString into a single
	 * EnvisionString.
	 * 
	 * @param a First String
	 * @param b Second String
	 * @return A joined version of each string's contents
	 */
	public static EnvisionString concatenate(EnvisionString a, EnvisionString b) {
		return newString(a.string_val.append(b.string_val));
	}
	
	/**
	 * Joins the values of each String into a single
	 * EnvisionString.
	 * 
	 * @param a First String
	 * @param b Second String
	 * @return A joined version of each string's contents
	 */
	public static EnvisionString concatenate(String a, EnvisionString b) {
		return newString(a + b.string_val);
	}
	
	/**
	 * Joins the values of each String into a single
	 * EnvisionString.
	 * 
	 * @param a First String
	 * @param b Second String
	 * @return A joined version of each string's contents
	 */
	public static EnvisionString concatenate(EnvisionString a, String b) {
		return newString(a.string_val + b);
	}
	
	/**
	 * Joins the values of each String into a single
	 * EnvisionString.
	 * 
	 * @param a First String
	 * @param b Second String
	 * @return A joined version of each string's contents
	 */
	public static EnvisionString concatenate(String a, String b) {
		return newString(a + b);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public ClassInstance newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//bypass class construct for primitive type
		return buildInstance(interpreter, args);
	}
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionString str = null;
		
		//if no args, return default string instance
		if (args.length == 0) str = new EnvisionString();
		
		//otherwise, attempt to create from passed args
		
		//ensure there is at most 1 argument being passed
		if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		
		Object arg_val = false;
		if (args.length == 1) arg_val = args[0];
		
		//don't accept null arguments
		if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
		
		str = new EnvisionString(arg_val);
		
		//define scope members
		defineScopeMembers(str);
		
		//return built list instance
		return str;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		
		//cast to string
		EnvisionString str = (EnvisionString) inst;
		
		//extract scope
		Scope inst_scope = str.getScope();
		
		//define instance members
		/*
		inst_scope.defineFunction(new IFunc_equals(str)); //override equals
		inst_scope.defineFunction(new IFunc_toString(str)); //override toString
		inst_scope.defineFunction(new IFunc_charAt(str));
		inst_scope.defineFunction(new IFunc_toList(str));
		inst_scope.defineFunction(new IFunc_length(str));
		inst_scope.defineFunction(new IFunc_isEmpty(str));
		inst_scope.defineFunction(new IFunc_isNotEmpty(str));
		inst_scope.defineFunction(new IFunc_ascii(str));
		inst_scope.defineFunction(new IFunc_toLowerCase(str));
		inst_scope.defineFunction(new IFunc_toUpperCase(str));
		inst_scope.defineFunction(new IFunc_get(str));
		inst_scope.defineFunction(new IFunc_set(str));
		inst_scope.defineFunction(new IFunc_substring(str));
		inst_scope.defineFunction(new IFunc_compareTo(str));
		*/
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	/**
	 * Override equals to account for internal string value.
	 */
	public static class IFunc_equals<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_equals(E instIn) { super(instIn, BOOLEAN, "equals", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionObject obj = args[0];
			boolean eq = false;
			if (obj instanceof EnvisionString env_str) eq = inst.string_val.equals(env_str.string_val);
			EnvisionBoolean eq_obj = EnvisionBooleanClass.newBoolean(eq);
			ret(eq_obj);
		}
	}
	
	/**
	 * Override toString to account for internal string value.
	 */
	public static class IFunc_toString<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_toString(E instIn) { super(instIn, STRING, "toString"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst);
		}
	}
	
	private static class IFunc_charAt<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_charAt(E inst) { super(inst, CHAR, "charAt", INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			int pos = (int) ((EnvisionInt) args[0]).int_val;
			ret(inst.charAt(pos));
		}
	}
	
	private static class IFunc_toList<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_toList(E inst) { super(inst, LIST, "toList"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.toList());
		}
	}
	
	private static class IFunc_length<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_length(E inst) { super(inst, INT, "length"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.length());
		}
	}
	
	private static class IFunc_isEmpty<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_isEmpty(E inst) { super(inst, BOOLEAN, "isEmpty"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isEmpty());
		}
	}
	
	private static class IFunc_isNotEmpty<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_isNotEmpty(E inst) { super(inst, BOOLEAN, "isNotEmpty"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isNotEmpty());
		}
	}
	
	private static class IFunc_isChar<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_isChar(E inst) { super(inst, BOOLEAN, "isChar"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isChar());
		}
	}
	
	private static class IFunc_ascii<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_ascii(E inst) { super(inst, INT, "ascii"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.ascii());
		}
	}
	
	private static class IFunc_toLowerCase<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_toLowerCase(E inst) { super(inst, STRING, "toLowerCase"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.toLowerCase());
		}
	}
	
	private static class IFunc_toUpperCase<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_toUpperCase(E inst) { super(inst, STRING, "toUpperCase"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.toUpperCase());
		}
	}
	
	private static class IFunc_get<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_get(E inst) { super(inst, STRING, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst);
		}
	}
	
	/**
	 * Internally assign the value of this EnvisionString's Java:String value.
	 * 
	 * @param val The new String value
	 * @return This EnvisionString
	 */
	private static class IFunc_set<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_set(E inst) { super(inst, STRING, "set", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.set_i(String.valueOf(args[0])));
		}
	}
	
	private static class IFunc_substring<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_substring(E inst) {
			super(inst, STRING, "set", INT);
			addOverload(STRING, INT, INT);
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args.length == 1) ret(inst.substring((EnvisionInt) args[0]));
			else if (args.length == 2) ret(inst.substring((EnvisionInt) args[0], (EnvisionInt) args[1]));
			else throw new EnvisionError("Invalid argument length -- should not have reached this point!");
		}
	}
	
	private static class IFunc_compareTo<E extends EnvisionString> extends InstanceFunction<E> {
		IFunc_compareTo(E inst) { super(inst, INT, "compareTo", STRING); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.compareTo((EnvisionString) args[0]));
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(STRING, "valueOf", STRING);
			//allow valueOf(Object)
			addOverload(STRING, VAR);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.newString(args[0]));
		}
	}
	
}