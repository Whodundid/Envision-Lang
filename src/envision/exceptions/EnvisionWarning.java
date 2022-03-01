package envision.exceptions;

/**
 * An EnvisionWarning is an problem that does not halt program execution and is reported to the user.
 */
public class EnvisionWarning extends RuntimeException {

	public EnvisionWarning(String message) {
		super(message);
	}
	
}
