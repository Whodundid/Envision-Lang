package envision_lang.lang.language_errors;

/**
 * An EnvisionError marks a point where script execution cannot continue as some
 * form of logic/syntax error has been detected.
 */
public class EnvisionLangError extends RuntimeException {
	
	public EnvisionLangError(String message) {
		super(message);
	}
	
	public EnvisionLangError(Throwable e) {
		super(e);
	}
	
	public EnvisionLangError(String message, Throwable e) {
	    super(message, e);
	}
	
}
