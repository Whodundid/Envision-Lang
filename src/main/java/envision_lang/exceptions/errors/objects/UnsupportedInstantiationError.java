package envision_lang.exceptions.errors.objects;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.classes.EnvisionClass;

public class UnsupportedInstantiationError extends EnvisionLangError {
	
	public UnsupportedInstantiationError(EnvisionClass in) {
		super("The class '" + in.getClassName() + "' does not support be directly instantiated!");
	}

}
