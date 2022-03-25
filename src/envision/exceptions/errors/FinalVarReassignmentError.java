package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class FinalVarReassignmentError extends EnvisionError {
	
	public FinalVarReassignmentError(EnvisionObject var, Object value) {
		super("Attempted to reassign the value of a final variable! " + var + " with " + value);
	}
	
	public FinalVarReassignmentError(Object var) {
		super("Attempted to reassign the final variable '" + var + "'!");
	}
	
}
