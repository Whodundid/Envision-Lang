package envision_lang.lang.exceptions.errors.classErrors;

import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.functions.EnvisionFunction;

public class NotAConstructorError extends EnvisionLangError {
	
	public NotAConstructorError(EnvisionFunction m) {
		super("The given method: " + m + " is not a constructor!");
	}

}