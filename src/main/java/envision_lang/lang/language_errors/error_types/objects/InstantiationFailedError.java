package envision_lang.lang.language_errors.error_types.objects;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.language_errors.EnvisionLangError;

public class InstantiationFailedError extends EnvisionLangError {
	
	public InstantiationFailedError(EnvisionClass in) {
		super("A new instance of the given class: " + in.getClassName() + " failed to be created!");
	}

}
