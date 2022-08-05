package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.natives.Primitives;

public class StrongVarReassignmentError extends EnvisionLangError {
	
	public StrongVarReassignmentError(EnvisionVariable var, Object value) {
		super("Attempted to reassign the value of a strong dynamic variable: [" + var + ":"
				+ var.getDatatype() + "] to [" + value + ":" + Primitives.getDataType(value)
				+ "]!");
	}
	
}
