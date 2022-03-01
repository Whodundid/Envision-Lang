package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionFunction;

public class NoReturnStatementError extends EnvisionError {

	public NoReturnStatementError(EnvisionFunction method) {
		super("The method: '" + method.getName() + "' declares a return type of (" + method.getReturnType() + ") but does not actually return anything!");
	}
	
}
