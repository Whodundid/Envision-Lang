package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

public class NotANumberError extends EnvisionLangError {
	
	public NotANumberError(Object o) {
		super (o + " is not a number!");
	}
	
}
