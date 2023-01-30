package envision_lang.lang.exceptions.errors;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.ParameterData;
import eutil.strings.EStringUtil;

public class NoOverloadError extends EnvisionLangError {
	
	public NoOverloadError(EnvisionFunction func, ParameterData params) {
		super("The function '" + func.getFunctionName() +
				"' does not have an overload with parameters: " + params);
	}
	
	public NoOverloadError(String func_name, ParameterData params) {
		super("The function '" + func_name + "' does not have an overload with parameters: " + params);
	}
	
	public NoOverloadError(String func_name, EnvisionObject[] args) {
		super("The function '" + func_name + "' does not have an overload with parameters: " +
				EStringUtil.toString(args));
	}
	
}
