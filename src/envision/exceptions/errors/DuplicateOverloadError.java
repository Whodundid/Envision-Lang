package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.util.ParameterData;

public class DuplicateFunctionError extends EnvisionError {
	
	public DuplicateFunctionError(String funcName, ParameterData params) {
		super("The function: " + funcName + " already has an overload with the given params: " + params);
	}
	
}
