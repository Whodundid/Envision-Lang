package envision_lang.lang.language_errors.error_types.classErrors;

import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;

public class NotAConstructorError extends EnvisionLangError {
	
	public NotAConstructorError(EnvisionFunction m) {
		super("The given method: " + m + " is not a constructor!");
	}

}