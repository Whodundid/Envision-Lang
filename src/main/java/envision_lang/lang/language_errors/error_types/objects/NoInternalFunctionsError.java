package envision_lang.lang.language_errors.error_types.objects;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;

public class NoInternalFunctionsError extends EnvisionLangError {
	
	public NoInternalFunctionsError(EnvisionObject in) {
		super("The given object: '" + in + "' does not have any internal methods!");
	}

}
