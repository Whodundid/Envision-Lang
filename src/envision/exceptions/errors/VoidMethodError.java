package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.InternalMethod;

/** Error thrown when attempting to return a value on a void method. */
public class VoidMethodError extends EnvisionError {

	public VoidMethodError(EnvisionFunction m) {
		super("The method '" + m.getName() + "' has a void return type and cannot return values!");
	}
	
	public VoidMethodError(InternalMethod m) {
		super("The method '" + m.getIMethodName() + "' has a void return type and cannot return values!");
	}

}
