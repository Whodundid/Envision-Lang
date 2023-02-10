package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

public class UndefinedFunctionError extends EnvisionLangError {
	
	public UndefinedFunctionError(String funcName) {
		super("The function: '" + funcName + "' is undefined!");
	}
	
	public UndefinedFunctionError(String funcName, Object object) {
		super("The function: '" + funcName + "' is undefined on the object " + "[" + object.getClass().getSimpleName() + ": '" + object + "']" + "!");
	}
	
}
