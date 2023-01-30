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

public final class EnvisionIntClass extends EnvisionClass {

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
		// no integer prototypes
	}
	
	/**
	 * Internally caches low-value integers to reduce overall integer
	 * instantiation.
	 * <p>
	 * This range of what constitutes a low/high value is configurable as a
	 * Envision VM argument.
	 * <p>
	 * Java Integer.IntegerCache heavily referenced.
	 * 
	 * @author Hunter Bragg
	 */
	private static final class EnvisionIntegerCache {
		static final int low = -128;
		static final int high;
		static final EnvisionInt[] cache;
		
		static {
			int h = 127;
			// make configurable in Envision VM args
			high = h;
			
			int size = (high - low) + 1;
			
			EnvisionInt[] c = new EnvisionInt[size];
			int j = low;
			for (int i = 0; i < c.length; i++) {
				c[i] = newInt(j++);
			}
			cache = c;
		}
		
		private EnvisionIntegerCache() {}
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
		staticScope.define("MIN_VALUE", EnvisionStaticTypes.INT_TYPE, EnvisionInt.MIN_VALUE);
		staticScope.define("MAX_VALUE", EnvisionStaticTypes.INT_TYPE, EnvisionInt.MAX_VALUE);
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionInt newInt() { return newInt(0); }
	public static EnvisionInt newInt(boolean value) { return newInt(value ? 1L : 0L); }
	public static EnvisionInt newInt(char value) { return newInt((long) value); }
	public static EnvisionInt newInt(EnvisionNumber value) { return newInt(value.intVal_i()); }
	public static EnvisionInt newInt(long value) {
		EnvisionInt i = new EnvisionInt(value);
		INT_CLASS.defineScopeMembers(i);
		return i;
	}

	public static EnvisionInt defaultValue() {
		return EnvisionInt.ZERO;
	}
	
	/** Return back direct instances. */
	public static EnvisionInt valueOf(EnvisionInt value) { return value; }
	public static EnvisionInt valueOf(boolean value) { return valueOf((value) ? 1L : 0L); }
	public static EnvisionInt valueOf(char value) { return valueOf((long) value); }

	/**
	 * Converts the given EnvisionNumber value into an EnvisionInt.
	 * 
	 * @param value The EnvisionNumber to convert to an EnvisionInt
	 * 
	 * @return An EnvisionInt made from the incoming value
	 */
	public static EnvisionInt valueOf(EnvisionNumber value) {
		// return direct instances
		if (value instanceof EnvisionInt i) return i;
		
		long int_val = value.intVal_i();
		return valueOf(int_val);
	}
	
	/**
	 * Wraps the given long value within an EnvisionInt.
	 * 
	 * @param value The long to wrap into an EnvisionInt
	 * 
	 * @return An EnvisionInt made from the incoming value
	 */
	public static EnvisionInt valueOf(long value) {
		int i = (int) value;
		
		if (i >= EnvisionIntegerCache.low && i <= EnvisionIntegerCache.high) {
			return EnvisionIntegerCache.cache[i + (-EnvisionIntegerCache.low)];
		}
		
		return newInt(value);
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
			else if (arg_val instanceof EnvisionNumber n) int_val = n.intVal();
			else if (arg_val instanceof EnvisionBoolean b) int_val = new EnvisionInt((b.bool_val) ? 1l : 0l);
			
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
			if (args[0] instanceof EnvisionNumber env_num) ret(EnvisionIntClass.valueOf(env_num));
			else throw new EnvisionLangError("Invalid type -- should not have reached here!");
		}
	}
	
}