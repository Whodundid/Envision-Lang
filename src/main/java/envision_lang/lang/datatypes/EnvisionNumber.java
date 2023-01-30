package envision_lang.lang.datatypes;

import envision_lang.exceptions.errors.ArithmeticError;
import envision_lang.lang.classes.EnvisionClass;

/**
 * An abstract script variable representing a number with or without a
 * decimal point.
 * 
 * @author Hunter Bragg
 */
public abstract sealed class EnvisionNumber<TYPE extends Number> extends EnvisionVariable<TYPE>
	permits EnvisionInt, EnvisionDouble
{
	
	//--------------
	// Constructors
	//--------------
	
	protected EnvisionNumber(EnvisionClass classType) {
		super(classType);
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Negates the current number value on this EnvisionNumber object.
	 * 
	 * @return The negated value of this number
	 */
	public abstract EnvisionNumber negate();
	
	/**
	 * Converts the internal number value, regardless of type, to its
	 * integer (long) equivalent.
	 * 
	 * @return the long version of the number
	 */
	public abstract long intVal_i();
	
	/**
	 * Converts the internal number value, regardless of type, to its
	 * double equivalent.
	 * 
	 * @return the double version of the number
	 */
	public abstract double doubleVal_i();
	
	/**
	 * Converts the internal number value, regardless of type, to its
	 * integer (long) equivalent.
	 * 
	 * @return the long version of the number
	 */
	public abstract EnvisionInt intVal();
	
	/**
	 * Converts the internal number value, regardless of type, to its
	 * double equivalent.
	 * 
	 * @return the double version of the number
	 */
	public abstract EnvisionDouble doubleVal();
	
	
	@Override
	public Number convertToJavaObject() {
		return doubleVal_i();
	}
	
	//-----------------------
	// Static Helper Methods
	//-----------------------
	
	/** Throws / by zero error. */
	protected static void div0_l(long a, long b) {
		if (b == 0) {
			throw new ArithmeticError("("+a+" / "+b+") error! Division by zero!");
		}
	}
	
	protected static void div0_d(double a, double b) {
		if (b == 0) {
			throw new ArithmeticError("("+a+" / "+b+") error! Division by zero!");
		}
	}
	
}
