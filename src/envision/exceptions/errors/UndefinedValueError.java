package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class UndefinedValueError extends EnvisionError {
	
	public UndefinedValueError(String value) {
		super("The value: " + value + " is undefined within the current scope!");
	}
	
}
