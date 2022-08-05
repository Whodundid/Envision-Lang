package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class RestrictedAccessError extends EnvisionLangError {

	public RestrictedAccessError(EnvisionObject object) {
		super("The member: '" + object + "' is natively restricted and cannot be accessed or modified!");
	}
	
}
