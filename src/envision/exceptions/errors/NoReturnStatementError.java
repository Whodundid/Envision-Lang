package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.internal.EnvisionFunction;

public class NoReturnStatementError extends EnvisionError {

	public NoReturnStatementError(EnvisionFunction func) {
		super("The method: '" + func.getFunctionName() + "' declares a return type of ("
				+ func.getReturnType() + ") but does not actually return anything!");
	}
	
}
