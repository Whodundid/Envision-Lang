package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class NotAFunctionError extends EnvisionError {
	
	public NotAFunctionError(Object o) {
		super (o + " is not a function!");
	}
	
}
