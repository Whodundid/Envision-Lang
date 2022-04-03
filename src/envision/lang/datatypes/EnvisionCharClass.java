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
import envision.lang.util.InstanceFunction;
import envision.lang.util.Primitives;

public class EnvisionCharClass extends EnvisionClass {

	/**
	 * The singular, static Char class for which all Envision:Char
	 * objects are derived from.
	 */
	public static final EnvisionCharClass CHAR_CLASS = new EnvisionCharClass();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionCharClass() {
		super(Primitives.CHAR);
		
		//set final to prevent user-extension
		setFinal();
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionChar newChar() { return newChar('\0'); }
	public static EnvisionChar newChar(boolean value) { return newChar(value ? 'T' : 'F'); }
	public static EnvisionChar newChar(EnvisionChar value) { return newChar(value.char_val); }
	public static EnvisionChar newChar(char value) {
		EnvisionChar c = new EnvisionChar(value);
		CHAR_CLASS.defineScopeMembers(c);
		return c;
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
		EnvisionChar char_val = null;
		
		//if no args, return default char instance
		if (args.length == 0) char_val = new EnvisionChar();
		
		//otherwise, attempt to create from passed args
		
		//ensure there is at most 1 argument being passed
		if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		
		Object arg_val = false;
		if (args.length == 1) {
			arg_val = args[0];
		}
		
		//don't accept null arguments
		if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
		
		//check for invalid argument constructor datatypes
		if (arg_val instanceof Integer i) 	char_val = new EnvisionChar((char) arg_val);
		if (arg_val instanceof Long l) 		char_val = new EnvisionChar((char) arg_val);
		if (arg_val instanceof Boolean b) 	char_val = new EnvisionChar((b) ? 'T' : 'F');
		
		if (char_val == null)
			throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		
		//define scope members
		defineScopeMembers(char_val);
		
		return char_val;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		
		//cast to boolean
		EnvisionChar c = (EnvisionChar) inst;
		
		//extract instance scope
		Scope inst_scope = c.getScope();
		
		//define instance members
		//inst_scope.defineFunction(new IFunc_get(c));
		//inst_scope.defineFunction(new IFunc_set(c));
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	private static class IFunc_get<E extends EnvisionChar> extends InstanceFunction<E> {
		IFunc_get(E instIn) { super(instIn, CHAR, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionCharClass.newChar(inst.char_val));
		}
	}
	
	private static class IFunc_set<E extends EnvisionChar> extends InstanceFunction<E> {
		IFunc_set(E instIn) { super(instIn, CHAR, "set", CHAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.char_val = ((EnvisionChar) args[0]).char_val;
			ret(inst);
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(CHAR, "valueOf", CHAR);
			//allow valueOf(int)
			addOverload(CHAR, INT);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionChar env_char) ret(EnvisionCharClass.newChar(env_char));
			else if (args[0] instanceof EnvisionInt env_int) ret(EnvisionIntClass.newInt(env_int));
			else throw new EnvisionError("Invalid type -- should not have reached here!");
		}
	}
	
}