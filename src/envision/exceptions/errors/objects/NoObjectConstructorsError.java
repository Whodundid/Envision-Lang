package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class NoObjectConstructorsError extends EnvisionError {
	
	public NoObjectConstructorsError(EnvisionObject in) {
		super("The given object: '" + in + "' does not have any visible object constructors!");
	}

}
