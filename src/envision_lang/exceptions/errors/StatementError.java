package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

/**
 * Error thrown when a statement cannot be executed.
 */
public class StatementError extends EnvisionLangError {
	
	public StatementError(String message) {
		super(message);
	}

}
