package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

/**
 * Error thrown when an expression cannot be evaluated.
 */
public class ExpressionError extends EnvisionLangError {
	
	public ExpressionError(String message) {
		super(message);
	}

}
