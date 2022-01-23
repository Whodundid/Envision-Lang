package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class DuplicateObjectError extends EnvisionError {
	
	public DuplicateObjectError(String objName) {
		super("'" + objName + "' is already defined within current scope!");
	}
	
}
