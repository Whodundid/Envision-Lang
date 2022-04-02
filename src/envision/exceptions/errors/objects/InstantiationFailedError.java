package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.classes.EnvisionClass;

public class InstantiationFailedError extends EnvisionError {
	
	public InstantiationFailedError(EnvisionClass in) {
		super("A new instance of the given class: " + in.getClassName() + " failed to be created!");
	}

}
