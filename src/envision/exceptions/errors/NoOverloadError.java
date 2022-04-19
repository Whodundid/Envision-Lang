package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.ParameterData;

public class NoOverloadError extends EnvisionError {
	
	public NoOverloadError(EnvisionFunction func, ParameterData params) {
		super("The function '" + func.getFunctionName() +
				"' does not have an overload with parameters: " + params);
	}
	
	public NoOverloadError(String func_name, ParameterData params) {
		super("The function '" + func_name + "' does not have an overload with parameters: " + params);
	}
	
	public NoOverloadError(String func_name, EnvisionObject[] args) {
		super("The function '" + func_name + "' does not have an overload with parameters: " +
				new ParameterData(args));
	}
	
}
