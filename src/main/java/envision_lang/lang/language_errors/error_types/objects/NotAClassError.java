package envision_lang.lang.language_errors.error_types.objects;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;

public class NotAClassError extends EnvisionLangError {
	
	public NotAClassError(Object in) {
		super("The given object: '" + in + "' is not a valid class!");
	}
	
	public NotAClassError(EnvisionObject in) {
		super("The given object: '" + ((in != null) ? in : "(null)") + "' is not a valid class!");
	}

}
