package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class NotVisibleError extends EnvisionLangError {

	public NotVisibleError(EnvisionObject object) {
		super("The member: '" + object + "' is not visible within the current scope!");
	}
	
}
