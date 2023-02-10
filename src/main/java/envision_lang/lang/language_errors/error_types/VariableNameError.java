package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

/** Error thrown when attempting to declare/call a variable with an invalid name. */
public class VariableNameError extends EnvisionLangError {

	public VariableNameError(String varName) {
		super("Invalid variable name '" + varName + "'!");
	}

}
