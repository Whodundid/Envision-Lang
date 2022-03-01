package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.data.ParameterData;

public class NoOverloadError extends EnvisionError {
	
	public NoOverloadError(EnvisionFunction method, ParameterData paramsIn) {
		super("The method '" + method.getName() + "' does not have an overload with parameters: " + paramsIn);
	}
	
}
