package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import eutil.debug.Unused;

@Unused
public class InvalidOperationError extends EnvisionLangError {
	
	public InvalidOperationError(String message) {
		super(message);
	}
	
}
