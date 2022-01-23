package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionMethod;

public class InvalidArgumentError extends EnvisionError {
	
	public InvalidArgumentError(String message) {
		super(message);
	}
	
	public InvalidArgumentError(Object obj, String methodName) {
		super("'" + obj + "' is not a valid argument for the given method: " + methodName + "!");
	}
	
	public InvalidArgumentError(Object obj, EnvisionMethod method) {
		super("'" + obj + "' is not a valid argument for the given method: " + method + "!");
	}
	
}
