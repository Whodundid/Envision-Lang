package envision.exceptions.errors.classErrors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionMethod;

public class DuplicateConstructorError extends EnvisionError {
	
	public DuplicateConstructorError(EnvisionMethod s) {
		super("Constructor with params: " + s.getParamTypes() + " already exists within the current class!");
	}

}