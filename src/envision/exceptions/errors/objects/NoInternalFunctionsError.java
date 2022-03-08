package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class NoInternalFunctionsError extends EnvisionError {
	
	public NoInternalFunctionsError(EnvisionObject in) {
		super("The given object: '" + in + "' does not have any internal methods!");
	}

}
