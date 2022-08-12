package envision_lang.lang.datatypes;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.exceptions.errors.ArgLengthError;
import envision_lang.exceptions.errors.InvalidArgumentError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.internal.IPrototypeHandler;
import envision_lang.lang.internal.InstanceFunction;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.util.StaticTypes;

public class EnvisionBooleanClass extends EnvisionClass {
	
	/**
	 * The singular, static Boolean class for which all Envision:Boolean
	 * objects are derived from.
	 */
	public static final EnvisionBooleanClass BOOLEAN_CLASS = new EnvisionBooleanClass();
	
	/**
	 * Boolean function prototypes.
	 */
	private static final IPrototypeHandler prototypes = new IPrototypeHandler();
	
	//statically define function prototypes
	static {
		prototypes.define("get", BOOLEAN).assignDynamicClass(IFunc_get.class);
		prototypes.define("set", BOOLEAN, BOOLEAN).assignDynamicClass(IFunc_set.class);
	}
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionBooleanClass() {
		super(Primitives.BOOLEAN);
		
		//set final to prevent user-extension
		setFinal();
	}
	
	@Override
	protected void registerStaticNatives() {
		staticScope.defineFunction(new IFunc_static_valueOf());
		staticScope.define("TRUE", StaticTypes.BOOL_TYPE, EnvisionBoolean.TRUE);
		staticScope.define("FALSE", StaticTypes.BOOL_TYPE, EnvisionBoolean.FALSE);
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionBoolean defaultValue() {
		return EnvisionBoolean.FALSE;
	}
	
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
		//ensure there is at most 1 argument being passed
		else if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		//otherwise, attempt to create from passed args
		else {
			EnvisionObject arg_val = args[0];
			
			//don't accept null arguments
			if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
			
			//check for invalid argument constructor datatypes
			if (arg_val instanceof EnvisionInt i)		bool = new EnvisionBoolean(i.int_val != 0);
			if (arg_val instanceof EnvisionBoolean b)	bool = new EnvisionBoolean(b.bool_val);
			if (arg_val instanceof EnvisionString s)	bool = new EnvisionBoolean(s.string_val.toString().equals("true"));
			
			//if null, creation failed!
			if (bool == null)
				throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		}
		
		//define scope members
		defineScopeMembers(bool);
		
		return bool;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		//define boolean members
		prototypes.defineOn(inst);
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	private static class IFunc_get<E extends EnvisionBoolean> extends InstanceFunction<E> {
		public IFunc_get() { super(BOOLEAN, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.bool_val));
		}
	}
	
	private static class IFunc_set<E extends EnvisionBoolean> extends InstanceFunction<E> {
		public IFunc_set() { super(BOOLEAN, "set", BOOLEAN); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.bool_val = ((EnvisionBoolean) args[0]).bool_val;
			ret(inst);
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		public IFunc_static_valueOf() {
			super(BOOLEAN, "valueOf", STRING);
			//allow valueOf(Boolean)
			addOverload(BOOLEAN, BOOLEAN);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionString env_str) {
				ret(EnvisionBooleanClass.newBoolean(env_str.string_val.toString().equals("true")));
			}
			else if (args[0] instanceof EnvisionInt env_int) {
				ret(EnvisionBooleanClass.newBoolean(env_int.int_val == 1));
			}
			ret(EnvisionBooleanClass.newBoolean((EnvisionBoolean) args[0]));
		}
	}
	
}