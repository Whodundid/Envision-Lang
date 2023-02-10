package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;

public class RestrictedAccessError extends EnvisionLangError {

	public RestrictedAccessError(EnvisionObject object) {
		super("The member: '" + object + "' is natively restricted and cannot be accessed or modified!");
	}
	
}
