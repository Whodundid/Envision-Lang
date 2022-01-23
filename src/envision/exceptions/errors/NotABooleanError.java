package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class NotABooleanError extends EnvisionError {
	
	public NotABooleanError(Object o) {
		super (o + " is not a boolean value!");
	}
	
}
