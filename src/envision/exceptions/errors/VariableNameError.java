package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

/** Error thrown when attempting to declare/call a variable with an invalid name. */
public class VariableNameError extends EnvisionError {

	public VariableNameError(String varName) {
		super("Invalid variable name '" + varName + "'!");
	}

}
