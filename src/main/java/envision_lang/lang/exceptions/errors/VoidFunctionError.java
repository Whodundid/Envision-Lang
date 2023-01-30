package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.internal.EnvisionFunction;

/** Error thrown when attempting to return a value on a void function. */
public class VoidFunctionError extends EnvisionLangError {

	public VoidFunctionError(EnvisionFunction f) {
		super("The function '" + f.getFunctionName() + "' has a void return type and cannot return values!");
	}

}
