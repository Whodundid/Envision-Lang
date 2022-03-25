package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class RestrictedAccessError extends EnvisionError {

	public RestrictedAccessError(EnvisionObject object) {
		super("The member: '" + object + "' is natively restricted and cannot be accessed or modified!");
	}
	
}
