package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;

/**
 * Error thrown when an expression cannot be evaluated.
 */
public class ExpressionError extends EnvisionLangError {
	
	public ExpressionError(String message) {
		super(message);
	}

}
