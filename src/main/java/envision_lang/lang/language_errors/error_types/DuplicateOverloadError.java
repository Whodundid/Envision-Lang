package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.ParameterData;

public class DuplicateOverloadError extends EnvisionLangError {
	
	public DuplicateOverloadError(String funcName, ParameterData params) {
		super("The function: " + funcName + " already has an overload with the given params: " + params);
	}
	
}
