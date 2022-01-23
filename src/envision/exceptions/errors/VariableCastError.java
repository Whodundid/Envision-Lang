package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionVariable;

/** Error thrown when attempting to cast a variable to an incompatible datatype. */
public class VariableCastError extends EnvisionError {

	public VariableCastError(String message) {
		super(message);
	}
	
	public VariableCastError(EnvisionVariable var, EnvisionDataType castType) {
		this(var, castType.type);
	}
	
	public VariableCastError(EnvisionVariable var, String castType) {
		super("Variable '" + ((var != null) ? var.getName() : "null") + "' cannot be cast as type " + castType + "!");
	}

}
