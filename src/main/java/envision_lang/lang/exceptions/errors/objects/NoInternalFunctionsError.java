package envision_lang.lang.exceptions.errors.objects;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.EnvisionLangError;

public class NoInternalFunctionsError extends EnvisionLangError {
	
	public NoInternalFunctionsError(EnvisionObject in) {
		super("The given object: '" + in + "' does not have any internal methods!");
	}

}
