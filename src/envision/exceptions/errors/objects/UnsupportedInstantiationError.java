package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.classes.EnvisionClass;

public class UnsupportedInstantiationError extends EnvisionError {
	
	public UnsupportedInstantiationError(EnvisionClass in) {
		super("The class '" + in.getClassName() + "' does not support be directly instantiated!");
	}

}
