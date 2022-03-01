package envision.exceptions.errors.classErrors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionFunction;

public class NotAConstructorError extends EnvisionError {
	
	public NotAConstructorError(EnvisionFunction m) {
		super("The given method: " + m + " is not a constructor!");
	}

}