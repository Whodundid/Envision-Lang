package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

@Deprecated
/**
 * @deprecated replace all usages with 'InvalidTargetError' instead.
 */
public class NotABooleanError extends EnvisionLangError {
	
	public NotABooleanError(Object o) {
		super (o + " is not a boolean value!");
	}
	
}