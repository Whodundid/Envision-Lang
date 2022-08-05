package envision_lang.exceptions.errors.objects;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class NoInternalFunctionsError extends EnvisionLangError {
	
	public NoInternalFunctionsError(EnvisionObject in) {
		super("The given object: '" + in + "' does not have any internal methods!");
	}

}
