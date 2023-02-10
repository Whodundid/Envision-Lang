package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;

public class NoReturnStatementError extends EnvisionLangError {

	public NoReturnStatementError(EnvisionFunction func) {
		super("The method: '" + func.getFunctionName() + "' declares a return type of ("
				+ func.getReturnType() + ") but does not actually return anything!");
	}
	
}
