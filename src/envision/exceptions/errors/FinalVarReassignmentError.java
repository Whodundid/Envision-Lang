package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDataType;

public class FinalVarReassignmentError extends EnvisionError {
	
	public FinalVarReassignmentError(EnvisionObject var, Object value) {
		super("Attempted to reassign the value of a final variable: [" +
				  var.getName() + ":" + var.getInternalType() + "] to [" + value + ":" + EnvisionDataType.getDataType(value) + "]!");
	}
	
	public FinalVarReassignmentError(Object var) {
		super("Attempted to reassign the final variable '" + var + "'!");
	}
	
}
