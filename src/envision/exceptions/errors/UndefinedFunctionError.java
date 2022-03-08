package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class UndefinedFunctionError extends EnvisionError {
	
	public UndefinedFunctionError(String value) {
		super("The method: '" + value + "' is undefined!");
	}
	
	public UndefinedFunctionError(String methodName, String objectName) {
		super("The method: '" + methodName + "' is undefined on the object '" + objectName + "'!");
	}
	
}
