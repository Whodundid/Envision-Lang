package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.natives.EnvisionFunction;

public class NoReturnStatementError extends EnvisionLangError {

	public NoReturnStatementError(EnvisionFunction func) {
		super("The method: '" + func.getFunctionName() + "' declares a return type of ("
				+ func.getReturnType() + ") but does not actually return anything!");
	}
	
}
