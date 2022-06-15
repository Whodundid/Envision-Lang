package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

@Deprecated
/**
 * @deprecated replace all usages with 'InvalidTargetError' instead.
 */
public class NotABooleanError extends EnvisionError {
	
	public NotABooleanError(Object o) {
		super (o + " is not a boolean value!");
	}
	
}
