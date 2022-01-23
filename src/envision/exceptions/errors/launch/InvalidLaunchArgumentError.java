package envision.exceptions.errors.launch;

import envision.exceptions.EnvisionError;

public class InvalidLaunchArgumentError extends EnvisionError {

	public InvalidLaunchArgumentError(String invalidArgName) {
		super("The argument '" + invalidArgName + "' does not exist!");
	}
	
}
