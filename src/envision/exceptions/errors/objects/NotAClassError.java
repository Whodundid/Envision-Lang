package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class NotAClassError extends EnvisionError {
	
	public NotAClassError(Object in) {
		super("The given object: '" + in + "' is not a valid class!");
	}
	
	public NotAClassError(EnvisionObject in) {
		super("The given object: '" + ((in != null) ? in : "(null)") + "' is not a valid class!");
	}

}
