package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import eutil.debug.Unused;

@Unused
public class InvalidOperationError extends EnvisionError {
	
	public InvalidOperationError(String message) {
		super(message);
	}
	
}
