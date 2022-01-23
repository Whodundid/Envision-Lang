package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

/** Error thrown when an expression cannot properly be generated. */
public class ExpressionError extends EnvisionError {
	
	public ExpressionError(String message) {
		super(message);
	}

}
