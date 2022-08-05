package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

/** Error thrown when attempting to declare/call a variable with an invalid name. */
public class VariableNameError extends EnvisionLangError {

	public VariableNameError(String varName) {
		super("Invalid variable name '" + varName + "'!");
	}

}
