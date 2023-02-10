package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

@Deprecated
/**
 * @deprecated replace all usages with 'InvalidTargetError' instead.
 */
public class NotABooleanError extends EnvisionLangError {
	
	public NotABooleanError(Object o) {
		super (o + " is not a boolean value!");
	}
	
}
