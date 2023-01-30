package envision_lang.lang.exceptions.errors;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.EnvisionLangError;

public class RestrictedAccessError extends EnvisionLangError {

	public RestrictedAccessError(EnvisionObject object) {
		super("The member: '" + object + "' is natively restricted and cannot be accessed or modified!");
	}
	
}
