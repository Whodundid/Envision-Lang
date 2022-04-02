package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.Primitives;

public class StrongVarReassignmentError extends EnvisionError {
	
	public StrongVarReassignmentError(EnvisionVariable var, Object value) {
		super("Attempted to reassign the value of a strong dynamic variable: [" + var + ":"
				+ var.getDatatype() + "] to [" + value + ":" + Primitives.getDataType(value)
				+ "]!");
	}
	
}
