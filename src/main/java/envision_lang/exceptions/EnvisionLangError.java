package envision_lang.exceptions;

/**
 * An EnvisionError marks a point where script execution cannot continue as some
 * form of logic/syntax error has been detected.
 */
public class EnvisionLangError extends RuntimeException {
	
	public EnvisionLangError(String message) {
		super(message);
	}
	
	public EnvisionLangError(Exception e) {
		super(e.toString());
	}
	
}
