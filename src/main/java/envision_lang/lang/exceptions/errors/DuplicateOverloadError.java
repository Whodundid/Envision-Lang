package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.util.ParameterData;

public class DuplicateOverloadError extends EnvisionLangError {
	
	public DuplicateOverloadError(String funcName, ParameterData params) {
		super("The function: " + funcName + " already has an overload with the given params: " + params);
	}
	
}
