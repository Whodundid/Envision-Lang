package envision_lang.lang.exceptions.errors.objects;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.exceptions.EnvisionLangError;

public class InstantiationFailedError extends EnvisionLangError {
	
	public InstantiationFailedError(EnvisionClass in) {
		super("A new instance of the given class: " + in.getClassName() + " failed to be created!");
	}

}
