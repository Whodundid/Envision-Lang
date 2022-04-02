package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class UndefinedFunctionError extends EnvisionError {
	
	public UndefinedFunctionError(String funcName) {
		super("The function: '" + funcName + "' is undefined!");
	}
	
	public UndefinedFunctionError(String funcName, String objectName) {
		super("The function: '" + funcName + "' is undefined on the object '" + objectName + "'!");
	}
	
}
