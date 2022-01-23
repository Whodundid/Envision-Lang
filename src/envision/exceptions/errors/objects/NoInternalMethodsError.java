package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class NoInternalMethodsError extends EnvisionError {
	
	public NoInternalMethodsError(EnvisionObject in) {
		super("The given object: '" + in + "' does not have any internal methods!");
	}

}
