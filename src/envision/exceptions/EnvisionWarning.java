package envision.exceptions;

/** A ScriptWarning is an problem that does not halt script execution and is reported to the user. */
public class EnvisionWarning extends Exception {

	public EnvisionWarning(String message) {
		super(message);
	}
	
}
