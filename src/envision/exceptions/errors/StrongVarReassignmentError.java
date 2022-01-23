package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionVariable;

public class StrongVarReassignmentError extends EnvisionError {
	
	public StrongVarReassignmentError(EnvisionVariable var, Object value) {
		super("Attempted to reassign the value of a strong dynamic variable: [" +
			  var.getName() + ":" + var.getInternalType() + "] to [" + value + ":" + EnvisionDataType.getDataType(value) + "]!");
	}
	
}
