package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class NotANumberError extends EnvisionError {
	
	public NotANumberError(Object o) {
		super (o + " is not a number!");
	}
	
}
