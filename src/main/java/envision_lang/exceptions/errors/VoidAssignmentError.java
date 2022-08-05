package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

/** Error thrown when attempting to return a value on a void method. */
public class VoidAssignmentError extends EnvisionLangError {

	public VoidAssignmentError(String theVar) {
		super("The variable '" + theVar + "' cannot be assigned to a void value!");
	}

}
