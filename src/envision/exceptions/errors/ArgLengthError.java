package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.classes.EnvisionClass;
import envision.lang.internal.EnvisionFunction;

/**
 * A type of EnvisionError thrown when a function is attempting to be
 * called but an incorrect number of arguments were passed.
 * 
 * @author Hunter Bragg
 */
public class ArgLengthError extends EnvisionError {

	/**
	 * Creates a new ArgLengthError built from some EnvisionFunction.
	 * 
	 * @param func     The function throwing this error
	 * @param expected The number of arguments expected
	 * @param got      The number of arguments actually received
	 */
	public ArgLengthError(EnvisionFunction func, int expected, int got) {
		super("Invalid number of arguments for '" + func.getFunctionName() + "': expected ("
				+ expected + ") but got (" + got + ")!");
	}
	
	/**
	 * Creates a new ArgLengthError built from some EnvisionClass.
	 * 
	 * @param func     The class throwing this error
	 * @param expected The number of arguments expected
	 * @param got      The number of arguments actually received
	 */
	public ArgLengthError(EnvisionClass clazz, int expected, int got) {
		super("Invalid number of arguments for '" + clazz.getClassName() + "': expected ("
				+ expected + ") but got (" + got + ")!");
	}
	
	public ArgLengthError(String objName, int expected, int got) {
		super("Invalid number of arguments for '" + objName + "': expected ("
				+ expected + ") but got (" + got + ")!");
	}

}
