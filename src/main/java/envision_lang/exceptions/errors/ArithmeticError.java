package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

/** An error thrown during script execution if there is a math or arithmetic expression related error thrown. */
public class ArithmeticError extends EnvisionLangError {

	public ArithmeticError(String message) {
		super(message);
	}

}