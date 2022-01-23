package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.data.ParameterData;

public class NoOverloadError extends EnvisionError {
	
	public NoOverloadError(EnvisionMethod method, ParameterData paramsIn) {
		super("The method '" + method.getName() + "' does not have an overload with parameters: " + paramsIn);
	}
	
}
