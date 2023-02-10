package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

/** An error thrown during script execution if there is a math or arithmetic expression related error thrown. */
public class ArithmeticError extends EnvisionLangError {

	public ArithmeticError(String message) {
		super(message);
	}

}
