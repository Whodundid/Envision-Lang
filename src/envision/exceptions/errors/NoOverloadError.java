package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.ParameterData;

public class NoOverloadError extends EnvisionError {
	
	public NoOverloadError(EnvisionFunction func, ParameterData params) {
		super("The method '"+func.getFunctionName()+"' does not have an overload with parameters: "+params);
	}
	
}
