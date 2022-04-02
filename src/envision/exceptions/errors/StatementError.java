package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

/**
 * Error thrown when a statement cannot be executed.
 */
public class StatementError extends EnvisionError {
	
	public StatementError(String message) {
		super(message);
	}

}
