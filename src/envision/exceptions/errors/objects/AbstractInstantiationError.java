package envision.exceptions.errors.objects;

import envision.exceptions.EnvisionError;
import envision.lang.classes.EnvisionClass;

public class AbstractInstantiationError extends EnvisionError {
	
	public AbstractInstantiationError(EnvisionClass in) {
		super("The class '" + in.getName() + "' is abstract and cannot be directly instantiated!");
	}

}
