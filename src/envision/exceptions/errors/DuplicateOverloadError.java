package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.util.ParameterData;

public class DuplicateOverloadError extends EnvisionError {
	
	public DuplicateOverloadError(String funcName, ParameterData params) {
		super("The function: " + funcName + " already has an overload with the given params: " + params);
	}
	
}
