package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class ArgLengthError extends EnvisionError {

	public ArgLengthError(Object theMethod, int got, int expected) {
		super("Invalid number of arguments for '" + theMethod + "': expected (" + expected + ") but got (" + got + ")!");
	}

}
