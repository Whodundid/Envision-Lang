package envision.lang.internal;

import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;

/**
 * Null object representation within the Envision:Java Language.
 * 
 * <p>
 * Null objects can be created just as any other object instance would.
 * The only difference being that 'null' should be used to represent an
 * object placeholder.
 * 
 * <p>
 * As null objects are supposed to represent the complete absence of an
 * object, null objects do not have a object functions and cannot store
 * any kind of internal variable value.
 * 
 * @author Hunter Bragg
 */
public class EnvisionNull extends EnvisionObject {
	
	/**
	 * The single, static null value to be used for all 'null' values
	 * within Envision.
	 */
	public static final EnvisionNull NULL = new EnvisionNull();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Creates a new 'null' object.
	 */
	private EnvisionNull() {
		super(EnvisionDatatype.NULL_TYPE);
	}
	
	//------------------------------------
	// Overriding standard object methods
	//------------------------------------
	
	@Override
	public String toString() {
		return "ENVISION:null";
	}
	
}
