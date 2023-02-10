package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

/** Error thrown when attempting to return a value on a void method. */
public class VoidAssignmentError extends EnvisionLangError {

	public VoidAssignmentError(String theVar) {
		super("The variable '" + theVar + "' cannot be assigned to a void value!");
	}

}
