package envision_lang.interpreter.util.throwables;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.EnvisionNull;

/**
 * A means of returning data across method/class scopes within the
 * Envision::Java Scripting Language.
 * <p>
 * By wrapping this ReturnValue with a given object intended to be
 * returned and then subsequently throwing 'the' instance of this
 * ReturnValue, an expecting receiver (setup with a standard
 * Java::Try/Catch catching the type of ReturnValue) can catch this
 * Exception. The expecting receiver can then unwrap this ReturnValue
 * Exception which actually contains the object being returned.
 * 
 * @implNote Class creation in Java is expensive and requires a
 *           significant amount of additional overhead to simply
 *           create one object, let alone thousands. The act of
 *           returning a value in the form of an Exception is a great
 *           strategy that allows the ability to quickly transfer data
 *           between actively-running Envision scopes, but this
 *           approach is dramatically inefficient when coupled with
 *           the fact that a new 'ReturnValue' class instance needs to
 *           be created each and every single time ANY value is
 *           returned ANYWHERE. To remedy this, Envision::Java
 *           declares a single, static instance of 'ReturnValue'. This
 *           static singleton is then re-wrapped every time a new
 *           value is to be returned during program execution. The
 *           primary down-side of this approach is that simple
 *           multi-threading is no longer an option. However, the
 *           tradeoff is a language execution speedup of roughly 200%.
 * 			
 * @author Hunter Bragg
 */
public class ReturnValue extends RuntimeException {
	
	/**
	 * The static singleton 'ReturnValue'.
	 */
	private static final ReturnValue instance = new ReturnValue();
	
	//--------
	// Fields
	//--------
	
	/**
	 * The object being returned.
	 */
	public EnvisionObject result;
	
	//--------------
	// Constructors
	//--------------
	
	private ReturnValue() {
		this(EnvisionNull.NULL);
	}
	
	private ReturnValue(EnvisionObject objIn) {
		result = objIn;
	}
	
	//-----------------
	// Static Wrappers
	//-----------------
	
	/**
	 * Wraps this ReturnValue instance with Envision::NULL
	 * 
	 * @return The wrapped NULL ReturnValue
	 */
	public static ReturnValue NULL() {
		return wrap(EnvisionNull.NULL);
	}
	
	/**
	 * Wraps the given EnvisionObject into this ReturnValue's instance.
	 * 
	 * @param object The EnvisonObject to return
	 * @return The wrapped object ReturnValue
	 */
	public static ReturnValue wrap(EnvisionObject object) {
		instance.result = object;
		return instance;
	}
	
	/**
	 * Wraps the given EnvisionObject into this ReturnValue's instance
	 * and immediately throws it to initiate the return.
	 * 
	 * @param object The EnvisonObject to return
	 */
	public static void ret(EnvisionObject object) {
		throw wrap(object);
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns true if the return value is null.
	 */
	public boolean isNull() { return result == EnvisionNull.NULL; }
	
	/**
	 * Returns true if the return value is Java::null.
	 */
	public boolean isJavaNull() { return result == null; }
	
}
