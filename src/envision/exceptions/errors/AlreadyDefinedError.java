package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class AlreadyDefinedError extends EnvisionError {
	
	public AlreadyDefinedError(String objName) {
		super("'" + objName + "' is already defined within current scope!");
	}
	
}
