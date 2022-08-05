package envision_lang.exceptions.errors.objects;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class NoObjectConstructorsError extends EnvisionLangError {
	
	public NoObjectConstructorsError(EnvisionObject in) {
		super("The given object: '" + in + "' does not have any visible object constructors!");
	}

}
