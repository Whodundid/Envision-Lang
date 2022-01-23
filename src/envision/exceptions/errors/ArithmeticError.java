package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

/** An error thrown during script execution if there is a math or arithmetic expression related error thrown. */
public class ArithmeticError extends EnvisionError {

	public ArithmeticError(String message) {
		super(message);
	}

}
