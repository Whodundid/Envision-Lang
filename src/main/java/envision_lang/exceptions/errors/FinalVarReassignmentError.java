package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class FinalVarReassignmentError extends EnvisionLangError {
	
	public FinalVarReassignmentError(EnvisionObject var, Object value) {
		super("Attempted to reassign the value of a final variable! " + var + " with " + value);
	}
	
	public FinalVarReassignmentError(Object var) {
		super("Attempted to reassign the final variable '" + var + "'!");
	}
	
}
