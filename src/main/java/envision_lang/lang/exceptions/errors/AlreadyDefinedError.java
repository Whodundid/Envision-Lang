package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;

public class AlreadyDefinedError extends EnvisionLangError {
	
	public AlreadyDefinedError(String objName) {
		super("'" + objName + "' is already defined within current scope!");
	}
	
}
