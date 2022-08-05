package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

public class NotAFunctionError extends EnvisionLangError {
	
	public NotAFunctionError(Object o) {
		super (o + " is not a function!");
	}
	
}
