package envision_lang.lang.exceptions.errors.classErrors;

import envision_lang.lang.exceptions.EnvisionLangError;

public class UndefinedConstructorError extends EnvisionLangError {
	
	public UndefinedConstructorError() {
		super("The given constructor is actually null!");
	}
	
	public UndefinedConstructorError(String in) {
		super(in);
	}

}