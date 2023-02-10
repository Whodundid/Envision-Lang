package envision_lang.lang.language_errors.error_types.launch;

import envision_lang.lang.language_errors.EnvisionLangError;

public class InvalidLaunchArgumentError extends EnvisionLangError {

	public InvalidLaunchArgumentError(String invalidArgName) {
		super("The argument '" + invalidArgName + "' does not exist!");
	}
	
}
