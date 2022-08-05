package envision_lang.exceptions.errors.objects;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class NotAClassError extends EnvisionLangError {
	
	public NotAClassError(Object in) {
		super("The given object: '" + in + "' is not a valid class!");
	}
	
	public NotAClassError(EnvisionObject in) {
		super("The given object: '" + ((in != null) ? in : "(null)") + "' is not a valid class!");
	}

}
