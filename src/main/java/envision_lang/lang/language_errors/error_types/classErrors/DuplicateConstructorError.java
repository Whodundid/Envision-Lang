package envision_lang.lang.language_errors.error_types.classErrors;

import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import eutil.strings.EStringUtil;

public class DuplicateConstructorError extends EnvisionLangError {
	
	public DuplicateConstructorError(EnvisionFunction s) {
		super("Constructor with params: " + EStringUtil.toString(s.getParamTypes())
			+ " already exists within the current class!");
	}
	
}