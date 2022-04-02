package envision.lang.datatypes;

import static envision.lang.util.Primitives.*;

import envision.exceptions.errors.objects.AbstractInstantiationError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.classes.EnvisionClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

/**
 * The primary class for which all EnvisionNumbers are derived from.
 * EnvisionNumber is intended to be an abstract class such that only
 * EnvisionInt and EnvisionDouble directly derive from. However,
 * EnvisionNumber is not inherently restricted to internal use and can
 * be referenced/extended within Envision:Java.
 * 
 * @author Hunter Bragg
 */
public class EnvisionNumberClass extends EnvisionClass {
	
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
		staticClassScope.defineFunction(new IFunc_static_valueOf());
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionNumber newNumber() { return newNumber(0.0d); }
	public static EnvisionNumber newNumber(long val) { return new EnvisionInt(val); }
	public static EnvisionNumber newNumber(double val) { return new EnvisionDouble(val); }
	public static EnvisionNumber newNumber(Number val) {
		if (val instanceof Integer || val instanceof Long) return new EnvisionInt(val);
		return new EnvisionDouble(val);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionNumber newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw new AbstractInstantiationError(this);
	}
	
	@Override
	protected EnvisionNumber buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		throw new AbstractInstantiationError(this);
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
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args[0] instanceof EnvisionInt env_int) ret(new EnvisionInt(env_int));
			else if (args[0] instanceof EnvisionDouble env_double) ret(new EnvisionDouble(env_double));
		}
	}
	
}
