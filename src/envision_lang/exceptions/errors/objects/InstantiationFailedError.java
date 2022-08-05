package envision_lang.exceptions.errors.objects;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.classes.EnvisionClass;

public class InstantiationFailedError extends EnvisionLangError {
	
	public InstantiationFailedError(EnvisionClass in) {
		super("A new instance of the given class: " + in.getClassName() + " failed to be created!");
	}

}
