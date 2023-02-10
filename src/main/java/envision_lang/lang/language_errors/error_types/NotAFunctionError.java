package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

public class NotAFunctionError extends EnvisionLangError {
	
	public NotAFunctionError(Object o) {
		super (o + " is not a function!");
	}
	
}
