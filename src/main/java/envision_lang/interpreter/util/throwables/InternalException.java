package envision_lang.interpreter.util.throwables;

import envision_lang.lang.exceptions.EnvisionException;

/**
 * A special exception that wraps an internal Envision exception. This
 * class can effectively be used to 'throw' an exception inside of the
 * Envision::Java scripting language.
 * 
 * @author Hunter Bragg
 */
public class InternalException extends RuntimeException {
	
	//=========
	// Statics
	//=========
	
	/** The static singleton 'InternalException'. */
	private static final InternalException instance = new InternalException();
	
	//========
	// Fields
	//========
	
	public EnvisionException thrownException;
	
	//==============
	// Constructors
	//==============
	
	private InternalException() {
		thrownException = EnvisionException.DEFAULT_EXCEPTION;
	}
	
	//=================
	// Static Wrappers
	//=================
	
	public static InternalException DEFAULT() {
		return wrap(EnvisionException.DEFAULT_EXCEPTION);
	}
	
	public static InternalException wrap(EnvisionException exception) {
		instance.thrownException = exception;
		return instance;
	}
	
	public static void throwException(EnvisionException exception) {
		throw wrap(exception);
	}
	
}
