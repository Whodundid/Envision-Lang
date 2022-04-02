package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class FinalExtensionError extends EnvisionError {
	
	public FinalExtensionError(EnvisionObject in) {
		super("The given extension target: '" + in + "' is final and cannot be extended!");
	}

}
