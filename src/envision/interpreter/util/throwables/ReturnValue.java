package envision.interpreter.util.throwables;

import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionNull;

/**
 * A means of returning data across method/class scopes. Throw an
 * instance of this so that it can be caught 'returned' somewhere
 * else.
 * 
 * @author Hunter Bragg
 */
public class ReturnValue extends RuntimeException {
	
	//--------
	// Fields
	//--------
	
	/**
	 * The object being returned.
	 */
	public final EnvisionObject result;
	
	//--------------
	// Constructors
	//--------------
	
	public ReturnValue() { this(EnvisionNull.NULL); }
	public ReturnValue(EnvisionObject objIn) {
		result = objIn;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns true if the return value is null.
	 */
	public boolean isNull() {
		return result == null;
	}
	
}
