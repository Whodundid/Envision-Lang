package envision.exceptions.errors.classErrors;

import envision.exceptions.EnvisionError;
import envision.lang.internal.EnvisionFunction;

public class DuplicateConstructorError extends EnvisionError {
	
	public DuplicateConstructorError(EnvisionFunction s) {
		super("Constructor with params: " + s.getParamTypes() + " already exists within the current class!");
	}

}