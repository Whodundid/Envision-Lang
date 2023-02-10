package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

public class NotANumberError extends EnvisionLangError {
	
	public NotANumberError(Object o) {
		super (o + " is not a number!");
	}
	
}
