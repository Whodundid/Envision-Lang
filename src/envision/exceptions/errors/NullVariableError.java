package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

/** An error thrown if null is returned for a variable call. */
public class NullVariableError extends EnvisionError {
	
	public NullVariableError() {
		super("This object is entirely Null");
	}
	
	public NullVariableError(String varName) {
		super("Variable '" + varName + "' has not been declared within this scope!");
	}
	
}
