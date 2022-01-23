package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class UnexpectedTypeError extends EnvisionError {
	
	public UnexpectedTypeError(Object got, Object expected) {
		super("Expected type '" + expected + "' but got type '" + got + "'!");
	}
	
}
