package envision_lang.lang.exceptions.errors.classErrors;

import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.internal.EnvisionFunction;
import eutil.strings.EStringUtil;

public class DuplicateConstructorError extends EnvisionLangError {
	
	public DuplicateConstructorError(EnvisionFunction s) {
		super("Constructor with params: " + EStringUtil.toString(s.getParamTypes())
			+ " already exists within the current class!");
	}
	
}