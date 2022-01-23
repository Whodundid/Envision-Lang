package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class UndefinedMethodError extends EnvisionError {
	
	public UndefinedMethodError(String value) {
		super("The method: '" + value + "' is undefined!");
	}
	
	public UndefinedMethodError(String methodName, String objectName) {
		super("The method: '" + methodName + "' is undefined on the object '" + objectName + "'!");
	}
	
}
