package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.EnvisionDatatype;

public class InvalidArgumentError extends EnvisionError {
	
	public InvalidArgumentError(String message) {
		super(message);
	}
	
	public InvalidArgumentError(Object obj, String funcName) {
		super("'" + obj + "' is not a valid argument for the given function: " + funcName + "!");
	}
	
	public InvalidArgumentError(Object obj, EnvisionFunction function) {
		super("'" + obj + "' is not a valid argument for the given function: " + function + "!");
	}
	
	public InvalidArgumentError(String funcName, EnvisionDatatype expected, EnvisionDatatype got) {
		super("'"+funcName+":"+"' Invalid argument! Expected '"+expected+"' but got '"+got+"'!");
	}
	
}
