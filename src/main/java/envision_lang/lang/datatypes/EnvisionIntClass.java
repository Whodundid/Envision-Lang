package envision_lang.lang.datatypes;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.exceptions.EnvisionLangError;
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
import envision_lang.lang.natives.StaticTypes;

public class EnvisionIntClass extends EnvisionClass {

	/**
	 * The singular, static int class for which all EnvisionInt
	 * objects are derived from.
	 */
	public static final EnvisionIntClass INT_CLASS = new EnvisionIntClass();
	
	/**
	 * Integer member function prototypes.
	 */
	private static final IPrototypeHandler INT_PROTOS = new IPrototypeHandler();
	
	static {
		INT_PROTOS.define("get", INT).assignDynamicClass(IFunc_get.class);
		INT_PROTOS.define("set", INT).assignDynamicClass(IFunc_set.class);
		INT_PROTOS.define("min", INT, INT).assignDynamicClass(IFunc_min.class);
		INT_PROTOS.define("max", INT, INT).assignDynamicClass(IFunc_min.class);
	}
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionIntClass() {
		super(Primitives.INT);
		
		//set final to prevent user-extension
		setFinal();
	}
	
	@Override
	protected void registerStaticNatives() {
		staticScope.defineFunction(new IFunc_static_valueOf());
		staticScope.define("MIN_VALUE", StaticTypes.INT_TYPE, EnvisionInt.MIN_VALUE);
		staticScope.define("MAX_VALUE", StaticTypes.INT_TYPE, EnvisionInt.MAX_VALUE);
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionInt newInt() { return newInt(0); }
	public static EnvisionInt newInt(boolean value) { return newInt(value ? 1 : 0); }
	public static EnvisionInt newInt(char value) { return newInt((int) value); }
	public static EnvisionInt newInt(EnvisionNumber value) { return newInt(value.intVal_i()); }
	public static EnvisionInt newInt(Number value) {
		EnvisionInt i = new EnvisionInt(value);
		INT_CLASS.defineScopeMembers(i);
		return i;
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
		EnvisionInt int_val = null;
		
		//if no args, return default char instance
		if (args.length == 0) int_val = new EnvisionInt();
		//ensure there is at most 1 argument being passed
		else if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		//otherwise, attempt to create from passed args
		else {
			EnvisionObject arg_val = args[0];
			
			//don't accept null arguments
			if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
			
			//check for invalid argument constructor datatypes
			if (arg_val instanceof EnvisionNumber n) int_val = n.intVal();
			if (arg_val instanceof EnvisionBoolean b) int_val = new EnvisionInt((b.bool_val) ? 1l : 0l);
			
			if (int_val == null)
				throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		}
		
		//define scope members
		defineScopeMembers(int_val);
		
		return int_val;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		//define scope members
		INT_PROTOS.defineOn(inst);
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	private static class IFunc_get<E extends EnvisionInt> extends InstanceFunction<E> {
		public IFunc_get() { super(INT, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionIntClass.newInt(inst.int_val));
		}
	}
	
	private static class IFunc_set<E extends EnvisionInt> extends InstanceFunction<E> {
		public IFunc_set() { super(INT, "set", INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.int_val = ((EnvisionInt) args[0]).int_val;
			ret(inst);
		}
	}
	
	private static class IFunc_min<E extends EnvisionInt> extends InstanceFunction<E> {
		public IFunc_min() { super(INT, "min", INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionInt in = (EnvisionInt) args[0];
			long min = (inst.int_val <= in.int_val) ? inst.int_val : in.int_val;
			ret(EnvisionIntClass.newInt(min));
		}
	}
	
	private static class IFunc_max<E extends EnvisionInt> extends InstanceFunction<E> {
		public IFunc_max() { super(INT, "max", INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionInt in = (EnvisionInt) args[0];
			long max = (inst.int_val >= in.int_val) ? inst.int_val : in.int_val;
			ret(EnvisionIntClass.newInt(max));
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(INT, "valueOf", NUMBER);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionNumber env_num) ret(EnvisionIntClass.newInt(env_num));
			else throw new EnvisionLangError("Invalid type -- should not have reached here!");
		}
	}
	
}