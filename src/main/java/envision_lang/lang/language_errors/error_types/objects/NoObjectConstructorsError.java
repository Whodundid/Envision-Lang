package envision_lang.lang.language_errors.error_types.objects;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;

public class NoObjectConstructorsError extends EnvisionLangError {
	
	public NoObjectConstructorsError(EnvisionObject in) {
		super("The given object: '" + in + "' does not have any visible object constructors!");
	}

}
