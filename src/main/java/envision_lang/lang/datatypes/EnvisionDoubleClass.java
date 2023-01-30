package envision_lang.lang.datatypes;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.ArgLengthError;
import envision_lang.lang.exceptions.errors.InvalidArgumentError;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.IPrototypeHandler;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.natives.EnvisionStaticTypes;

public final class EnvisionDoubleClass extends EnvisionClass {

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
		// no double prototypes
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
		staticScope.define("MIN_VALUE", EnvisionStaticTypes.DOUBLE_TYPE, EnvisionDouble.MIN_VALUE);
		staticScope.define("MAX_VALUE", EnvisionStaticTypes.DOUBLE_TYPE, EnvisionDouble.MAX_VALUE);
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionDouble newDouble() { return newDouble(0.0D); }
	public static EnvisionDouble newDouble(boolean value) { return newDouble(value ? 1.0D : 0.0D); }
	public static EnvisionDouble newDouble(char value) { return newDouble((double) value); }
	public static EnvisionDouble newDouble(EnvisionNumber num) { return newDouble(num.doubleVal_i()); }
	public static EnvisionDouble newDouble(double value) {
		EnvisionDouble d = new EnvisionDouble(value);
		DOUBLE_CLASS.defineScopeMembers(d);
		return d;
	}
	
	public static EnvisionDouble defaultValue() {
		return EnvisionDouble.ZERO;
	}
	
	/** Return back direct instances. */
	public static EnvisionDouble valueOf(EnvisionDouble num) { return num; }
	public static EnvisionDouble valueOf(boolean value) { return valueOf((value) ? 1.0D : 0.0D); }
	public static EnvisionDouble valueOf(char value) { return valueOf((double) value); }

	public static EnvisionDouble valueOf(EnvisionNumber num) {
		// return direct instances
		if (num instanceof EnvisionDouble d) return d;
		
		double double_val = num.doubleVal_i();
		return valueOf(double_val);
	}
	
	public static EnvisionDouble valueOf(double num) {
		// check for edge cases first
		if (num == Double.MAX_VALUE) return EnvisionDouble.MAX_VALUE;
		if (num == Double.MIN_VALUE) return EnvisionDouble.MIN_VALUE;
		if (num == Double.MIN_NORMAL) return EnvisionDouble.MIN_NORMAL;
		if (num == Double.NaN) return EnvisionDouble.NaN;
		if (num == Double.POSITIVE_INFINITY) return EnvisionDouble.POSITIVE_INFINITY;
		if (num == Double.NEGATIVE_INFINITY) return EnvisionDouble.NEGATIVE_INFINITY;
		
		if (num == 0.0D) return EnvisionDouble.ZERO;
		if (num == 1.0D) return EnvisionDouble.ONE;
		
		return newDouble(num);
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
			else if (arg_val instanceof EnvisionNumber n) double_val = n.doubleVal();
			else if (arg_val instanceof EnvisionBoolean b) double_val = new EnvisionDouble((b.bool_val) ? 1.0 : 0.0);
			
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
			if (args[0] instanceof EnvisionNumber env_num) ret(EnvisionDoubleClass.valueOf(env_num));
			else throw new EnvisionLangError("Invalid type -- should not have reached here!");
		}
	}
	
}