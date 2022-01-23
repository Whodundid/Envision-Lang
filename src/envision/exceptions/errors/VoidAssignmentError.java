package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

/** Error thrown when attempting to return a value on a void method. */
public class VoidAssignmentError extends EnvisionError {

	public VoidAssignmentError(String theVar) {
		super("The variable '" + theVar + "' cannot be assigned to a void value!");
	}

}
