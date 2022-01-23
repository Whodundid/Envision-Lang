package envision.exceptions;

/** A ScriptError marks a point where script execution cannot continue as some form of logic/syntax error has been detected. */
public class EnvisionError extends RuntimeException {
	
	public EnvisionError(String message) {
		super(message);
	}
	
	public EnvisionError(Exception e) {
		super(e.toString());
	}
	
}
