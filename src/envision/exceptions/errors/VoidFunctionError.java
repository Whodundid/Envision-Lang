package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.internal.EnvisionFunction;

/** Error thrown when attempting to return a value on a void method. */
public class VoidFunctionError extends EnvisionError {

	public VoidFunctionError(EnvisionFunction f) {
		super("The method '" + f.getFunctionName() + "' has a void return type and cannot return values!");
	}

}
