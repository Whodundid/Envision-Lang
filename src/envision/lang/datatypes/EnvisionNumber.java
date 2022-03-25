package envision.lang.datatypes;

import envision.lang.classes.EnvisionClass;

public abstract class EnvisionNumber extends EnvisionVariable {
	
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
	
}
