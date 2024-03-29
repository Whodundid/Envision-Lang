package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;

/** Error thrown when attempting to cast a variable to an incompatible datatype. */
public class VariableCastError extends EnvisionLangError {

	public VariableCastError(String message) {
		super(message);
	}
	
	public VariableCastError(EnvisionVariable var, IDatatype castType) {
		super("Variable '" + ((var != null) ? var : "null") + "' cannot be cast as type " + castType + "!");
	}

}
