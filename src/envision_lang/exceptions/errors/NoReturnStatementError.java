package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.internal.EnvisionFunction;

public class NoReturnStatementError extends EnvisionLangError {

	public NoReturnStatementError(EnvisionFunction func) {
		super("The method: '" + func.getFunctionName() + "' declares a return type of ("
				+ func.getReturnType() + ") but does not actually return anything!");
	}
	
}
