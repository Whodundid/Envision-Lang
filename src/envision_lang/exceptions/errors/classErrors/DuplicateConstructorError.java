package envision_lang.exceptions.errors.classErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.internal.EnvisionFunction;

public class DuplicateConstructorError extends EnvisionLangError {
	
	public DuplicateConstructorError(EnvisionFunction s) {
		super("Constructor with params: " + s.getParamTypes() + " already exists within the current class!");
	}

}