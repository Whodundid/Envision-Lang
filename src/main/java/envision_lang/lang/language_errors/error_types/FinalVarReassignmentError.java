package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;

public class FinalVarReassignmentError extends EnvisionLangError {
	
	public FinalVarReassignmentError(EnvisionObject var, Object value) {
		super("Attempted to reassign the value of a final variable! " + var + " with " + value);
	}
	
	public FinalVarReassignmentError(Object var) {
		super("Attempted to reassign the final variable '" + var + "'!");
	}
	
}
