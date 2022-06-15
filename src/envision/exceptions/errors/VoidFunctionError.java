package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.internal.EnvisionFunction;

/** Error thrown when attempting to return a value on a void function. */
public class VoidFunctionError extends EnvisionError {

	public VoidFunctionError(EnvisionFunction f) {
		super("The function '" + f.getFunctionName() + "' has a void return type and cannot return values!");
	}

}
