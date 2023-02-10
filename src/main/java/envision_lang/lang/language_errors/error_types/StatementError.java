package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

/**
 * Error thrown when a statement cannot be executed.
 */
public class StatementError extends EnvisionLangError {
	
	public StatementError(String message) {
		super(message);
	}

}
