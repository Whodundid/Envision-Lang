package envision_lang.lang.datatypes;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.error_types.ArgLengthError;
import envision_lang.lang.language_errors.error_types.InvalidArgumentError;
import envision_lang.lang.natives.Primitives;

/**
 * The primary class for which all EnvisionNumbers are derived from.
 * EnvisionNumber is intended to be an abstract class such that only
 * EnvisionInt and EnvisionDouble directly derive from. However,
 * EnvisionNumber is not inherently restricted to internal use and can
 * be referenced/extended within Envision:Java.
 * 
 * @author Hunter Bragg
 */
public sealed class EnvisionNumberClass extends EnvisionClass
	permits EnvisionIntClass, EnvisionDoubleClass
{
	
	/**
	 * The singular, static Number class for which all Envision:Number
	 * objects are derived from. This is an abstract class for which
	 * EnvisionInt and EnvisionDouble specifically inherit from.
	 */
	public static final EnvisionNumberClass NUMBER_CLASS = new EnvisionNumberClass();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionNumberClass() {
		super(Primitives.NUMBER);
		
		//define static members
		staticScope.defineFunction(new IFunc_static_valueOf());
	}
	
	protected EnvisionNumberClass(Primitives typeIn) {
		super(typeIn);
		
		//define static members
		staticScope.defineFunction(new IFunc_static_valueOf());
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionNumber<?> newNumber() { return EnvisionInt.ZERO; }
	public static EnvisionNumber<?> newNumber(long val) { return EnvisionIntClass.valueOf(val); }
	public static EnvisionNumber<?> newNumber(double val) { return EnvisionDoubleClass.valueOf(val); }
	public static EnvisionNumber<?> newNumber(Number val) {
		if (val instanceof Float || val instanceof Double) {
			return EnvisionDoubleClass.valueOf(val.doubleValue());
		}
		return EnvisionIntClass.valueOf(val.longValue());
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionNumber newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//bypass class construct for primitive type
		return buildInstance(interpreter, args);
	}
	
	@Override
	protected EnvisionNumber buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionNumber<?> num = null;
		
		//if no args, return double::zero by default
		if (args.length == 0) num = EnvisionDouble.ZERO;
		//ensure there is at most 1 argument being passed
		else if (args.length > 1) throw new ArgLengthError(this, 1, args.length);
		//otherwise, attempt to create from passed args
		else {
			EnvisionObject arg_val = args[0];
			
			//don't accept null arguments
			if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be Java::Null!");
			
			//check for valid argument constructor types
			else if (arg_val instanceof EnvisionInt i)		num = i;
			else if (arg_val instanceof EnvisionDouble d)	num = d;
			else if (arg_val instanceof EnvisionNumber<?> n)	num = n;
			
			//if null, creation failed!
			if (num == null) {
				throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to a "+getDatatype()+"!");
			}
		}
		
		return num;
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	private static class IFunc_static_valueOf extends EnvisionFunction {
		IFunc_static_valueOf() {
			super(NUMBER, "valueOf", NUMBER);
			//make static
			setStatic();
		}
		@Override
		public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionInt env_int) ret(env_int);
			else if (args[0] instanceof EnvisionDouble env_double) ret(env_double);
		}
	}
	
}
