package envision_lang.lang.language_errors.error_types.classErrors;

import envision_lang.lang.language_errors.EnvisionLangError;

public class UndefinedConstructorError extends EnvisionLangError {
	
	public UndefinedConstructorError() {
		super("The given constructor is actually null!");
	}
	
	public UndefinedConstructorError(String in) {
		super(in);
	}

}