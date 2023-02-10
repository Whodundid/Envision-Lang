package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

public class AlreadyDefinedError extends EnvisionLangError {
	
	public AlreadyDefinedError(String objName) {
		super("'" + objName + "' is already defined within current scope!");
	}
	
}
