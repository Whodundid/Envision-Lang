package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class FinalExtensionError extends EnvisionError {
	
	public FinalExtensionError(EnvisionObject in) {
		super("The given extension target: '" + in.getName() + "' is final and cannot be extended!");
	}

}
