package envision_lang.lang.language_errors.error_types.objects;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.language_errors.EnvisionLangError;

public class UnsupportedInstantiationError extends EnvisionLangError {
	
	public UnsupportedInstantiationError(EnvisionClass in) {
		super("The class '" + in.getClassName() + "' does not support be directly instantiated!");
	}

}
