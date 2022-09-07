package envision_lang.interpreter.util.throwables;

/** Allows for in-language exceptions to be caught. */
public class EnvisionException extends RuntimeException {
	
	public EnvisionException() { super(); }
	public EnvisionException(String error) { super(error); }
	
}
