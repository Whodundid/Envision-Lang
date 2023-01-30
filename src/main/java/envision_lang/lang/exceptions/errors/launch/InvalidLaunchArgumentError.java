package envision_lang.lang.exceptions.errors.launch;

import envision_lang.lang.exceptions.EnvisionLangError;

public class InvalidLaunchArgumentError extends EnvisionLangError {

	public InvalidLaunchArgumentError(String invalidArgName) {
		super("The argument '" + invalidArgName + "' does not exist!");
	}
	
}
