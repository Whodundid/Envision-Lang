package envision.exceptions.errors.classErrors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionMethod;

public class NotAConstructorError extends EnvisionError {
	
	public NotAConstructorError(EnvisionMethod m) {
		super("The given method: " + m + " is not a constructor!");
	}

}