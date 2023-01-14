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

public class EnvisionDoubleClass extends EnvisionClass {

	/**
	 * The singular, static Double class for which all Envision:Double
	 * objects are derived from.
	 */
	public static final EnvisionDoubleClass DOUBLE_CLASS = new EnvisionDoubleClass();
	
	/**
	 * Double member function prototypes.
	 */
	private static final IPrototypeHandler DOUBLE_PROTOS = new IPrototypeHandler();
	
	static {
		DOUBLE_PROTOS.define("get", DOUBLE).assignDynamicClass(IFunc_get.class);
		DOUBLE_PROTOS.define("set", DOUBLE).assignDynamicClass(IFunc_set.class);
		DOUBLE_PROTOS.define("min", DOUBLE, DOUBLE).assignDynamicClass(IFunc_min.class);
		DOUBLE_PROTOS.define("max", DOUBLE, DOUBLE).assignDynamicClass(IFunc_min.class);
	}
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionDoubleClass() {
		super(Primitives.DOUBLE);
		
		//set final to prevent user-extension
		setFinal();
	}
	
	@Override
	protected void registerStaticNatives() {
		staticScope.defineFunction(new IFunc_static_valueOf());
		staticScope.define("MIN_VALUE", StaticTypes.DOUBLE_TYPE, EnvisionDouble.MIN_VALUE);
		staticScope.define("MAX_VALUE", StaticTypes.DOUBLE_TYPE, EnvisionDouble.MAX_VALUE);
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionDouble newDouble() { return newDouble(0.0); }
	public static EnvisionDouble newDouble(boolean value) { return newDouble(value ? 1.0 : 0.0); }
	public static EnvisionDouble newDouble(char value) { return newDouble((double) value); }
	public static EnvisionDouble newDouble(EnvisionNumber num) { return newDouble(num.doubleVal_i()); }
	public static EnvisionDouble newDouble(double value) {
		EnvisionDouble d = new EnvisionDouble(value);
		DOUBLE_CLASS.defineScopeMembers(d);
		return d;
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
		EnvisionDouble double_val = null;
		
		//if no args, return default char instance
		if (args.length == 0) double_val = new EnvisionDouble();
		//ensure there is at most 1 argument being passed
		else if (args.length > 1) throw new ArgLengthError(this, 1, args.length);		
		//otherwise, attempt to create from passed args
		else {
			EnvisionObject arg_val = args[0];
			
			//don't accept null arguments
			if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
			
			//check for invalid argument constructor datatypes
			if (arg_val instanceof EnvisionNumber n) double_val = n.doubleVal();
			if (arg_val instanceof EnvisionBoolean b) double_val = new EnvisionDouble((b.bool_val) ? 1.0 : 0.0);
			
			if (double_val == null)
				throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		}
		
		//define scope memebers
		defineScopeMembers(double_val);
		
		return double_val;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		//define scope members
		DOUBLE_PROTOS.defineOn(inst);
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	private static class IFunc_get<E extends EnvisionDouble> extends InstanceFunction<E> {
		public IFunc_get() { super(DOUBLE, "get"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionDoubleClass.newDouble(inst.double_val));
		}
	}
	
	private static class IFunc_set<E extends EnvisionDouble> extends InstanceFunction<E> {
		public IFunc_set() { super(DOUBLE, "set", DOUBLE); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.double_val = ((EnvisionDouble) args[0]).double_val;
			ret(inst);
		}
	}
	
	private static class IFunc_min<E extends EnvisionDouble> extends InstanceFunction<E> {
		public IFunc_min() { super(DOUBLE, "min", DOUBLE); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionDouble in = (EnvisionDouble) args[0];
			double min = (inst.double_val <= in.double_val) ? inst.double_val : in.double_val;
			ret(EnvisionDoubleClass.newDouble(min));
		}
	}
	
	private static class IFunc_max<E extends EnvisionDouble> extends InstanceFunction<E> {
		public IFunc_max() { super(DOUBLE, "max", DOUBLE); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionDouble in = (EnvisionDouble) args[0];
			double max = (inst.double_val >= in.double_val) ? inst.double_val : in.double_val;
			ret(EnvisionDoubleClass.newDouble(max));
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(DOUBLE, "valueOf", NUMBER);
			//make static
			setStatic();
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionNumber env_num) ret(EnvisionDoubleClass.newDouble(env_num));
			else throw new EnvisionLangError("Invalid type -- should not have reached here!");
		}
	}
	
}