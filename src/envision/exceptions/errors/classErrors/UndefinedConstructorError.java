package envision.exceptions.errors.classErrors;

import envision.exceptions.EnvisionError;

public class UndefinedConstructorError extends EnvisionError {
	
	public UndefinedConstructorError() {
		super("The given constructor is actually null!");
	}
	
	public UndefinedConstructorError(String in) {
		super(in);
	}

}