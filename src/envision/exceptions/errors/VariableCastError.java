package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.natives.IDatatype;

/** Error thrown when attempting to cast a variable to an incompatible datatype. */
public class VariableCastError extends EnvisionError {

	public VariableCastError(String message) {
		super(message);
	}
	
	public VariableCastError(EnvisionVariable var, IDatatype castType) {
		super("Variable '" + ((var != null) ? var : "null") + "' cannot be cast as type " + castType + "!");
	}

}
