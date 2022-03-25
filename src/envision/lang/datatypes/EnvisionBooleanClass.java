package envision.lang.datatypes;

import static envision.lang.util.Primitives.*;

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

public class EnvisionBooleanClass extends EnvisionClass {
	
	/**
	 * The singular, static Boolean class for which all Envision:Boolean
	 * objects are derived from.
	 */
	public static final EnvisionBooleanClass BOOLEAN_CLASS = new EnvisionBooleanClass();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionBooleanClass() {
		super(Primitives.BOOLEAN);
		
		//define static members
		staticClassScope.defineFunction(new IFunc_static_valueOf());
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionBoolean newBoolean() { return newBoolean(false); }
	public static EnvisionBoolean newBoolean(EnvisionBoolean val) { return newBoolean(val.bool_val); }
	public static EnvisionBoolean newBoolean(char val) { return newBoolean(val == 'T'); }
	public static EnvisionBoolean newBoolean(boolean val) {
		EnvisionBoolean bool = new EnvisionBoolean(val);
		BOOLEAN_CLASS.defineScopeMembers(bool);
		return bool;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionBoolean newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//bypass class construct for primitive type
		return buildInstance(interpreter, args);
	}
	
	@Override
	protected EnvisionBoolean buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionBoolean bool = null;
		
		//if no args, return default boolean instance
		if (args.length == 0) bool = new EnvisionBoolean();
		
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
		if (arg_val instanceof Integer i) 	bool = new EnvisionBoolean(i != 0);
		if (arg_val instanceof Long l) 		bool = new EnvisionBoolean(l != 0);
		if (arg_val instanceof Boolean b) 	bool = new EnvisionBoolean(b);
		if (arg_val instanceof String s) 	bool = new EnvisionBoolean(arg_val.equals("true"));
		
		//if null, creation failed!
		if (bool == null)
			throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		
		//define scope memebers
		defineScopeMembers(bool);
		
		return bool;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		
		//cast to boolean
		EnvisionBoolean bool = (EnvisionBoolean) inst;
		
		//extract instance scope
		Scope inst_scope = bool.getScope();
		
		//define instance members
		inst_scope.defineFunction(new IFunc_get(bool));
		inst_scope.defineFunction(new IFunc_set(bool));
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	private static class IFunc_get<E extends EnvisionBoolean> extends InstanceFunction<E> {
		IFunc_get(E instIn) { super(instIn, BOOLEAN, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.bool_val));
		}
	}
	
	private static class IFunc_set<E extends EnvisionBoolean> extends InstanceFunction<E> {
		IFunc_set(E instIn) { super(instIn, BOOLEAN, "set", BOOLEAN); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.bool_val = ((EnvisionBoolean) args[0]).bool_val;
			ret(inst);
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(BOOLEAN, "valueOf", STRING);
			//allow valueOf(Boolean)
			addOverload(BOOLEAN, BOOLEAN);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionString env_str) {
				ret(EnvisionBooleanClass.newBoolean(env_str.string_val.equals("true")));
			}
			ret(EnvisionBooleanClass.newBoolean((EnvisionBoolean) args[0]));
		}
	}
	
}
