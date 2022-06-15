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
	
	private ReturnValue() { this(EnvisionNull.NULL); }
	private ReturnValue(EnvisionObject objIn) {
		result = objIn;
	}
	
	public static ReturnValue create() { return create(EnvisionNull.NULL); }
	public static ReturnValue create(EnvisionObject objIn) {
		instance.result = objIn;
		return instance;
		//return new ReturnValue(objIn);
	}
	
	/*
	public ReturnValue() { this(EnvisionNull.NULL); }
	public ReturnValue(EnvisionObject objIn) {
		result = objIn;
	}
	*/
	
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
