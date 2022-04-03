package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class UndefinedFunctionError extends EnvisionError {
	
	public UndefinedFunctionError(String funcName) {
		super("The function: '" + funcName + "' is undefined!");
	}
	
	public UndefinedFunctionError(String funcName, Object object) {
		super("The function: '" + funcName + "' is undefined on the object '" + object + "'!");
	}
	
}
