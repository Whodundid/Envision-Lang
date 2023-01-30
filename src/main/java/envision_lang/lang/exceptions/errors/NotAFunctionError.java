package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;

public class NotAFunctionError extends EnvisionLangError {
	
	public NotAFunctionError(Object o) {
		super (o + " is not a function!");
	}
	
}
