package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.InternalFunction;

/** Error thrown when attempting to return a value on a void method. */
public class VoidFunctionError extends EnvisionError {

	public VoidFunctionError(EnvisionFunction m) {
		super("The method '" + m.getName() + "' has a void return type and cannot return values!");
	}
	
	public VoidFunctionError(InternalFunction m) {
		super("The method '" + m.getIFuncName() + "' has a void return type and cannot return values!");
	}

}
