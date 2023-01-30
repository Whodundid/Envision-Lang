package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;
import eutil.debug.Unused;

@Unused
public class InvalidOperationError extends EnvisionLangError {
	
	public InvalidOperationError(String message) {
		super(message);
	}
	
}
