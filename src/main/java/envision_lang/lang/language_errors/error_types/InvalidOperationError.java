package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;
import eutil.debug.Unused;

@Unused
public class InvalidOperationError extends EnvisionLangError {
	
	public InvalidOperationError(String message) {
		super(message);
	}
	
}
