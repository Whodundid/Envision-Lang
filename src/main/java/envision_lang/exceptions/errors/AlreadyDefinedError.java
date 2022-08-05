package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

public class AlreadyDefinedError extends EnvisionLangError {
	
	public AlreadyDefinedError(String objName) {
		super("'" + objName + "' is already defined within current scope!");
	}
	
}
