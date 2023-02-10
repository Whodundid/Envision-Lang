package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;

public class NotVisibleError extends EnvisionLangError {

	public NotVisibleError(EnvisionObject object) {
		super("The member: '" + object + "' is not visible within the current scope!");
	}
	
	public NotVisibleError(String objectName) {
		super("The object: '" + objectName + "' is not visible within the current scope!");
	}
	
}
