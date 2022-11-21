package envision_lang.lang.internal;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.StaticTypes;

/**
 * Null object representation for the Envision:Java Scripting Language.
 * 
 * <p>
 * Null objects can be created just as any other object instance
 * would. The only difference being that 'null' should be used to
 * represent an object placeholder.
 * 
 * <p>
 * As null objects are supposed to represent the complete absence of
 * an object, null objects do not have object functions and cannot be
 * used to store any kind of internal variable value.
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
		super(StaticTypes.NULL_TYPE);
	}
	
	//------------------------------------
	// Overriding standard object methods
	//------------------------------------
	
	@Override
	public String toString() {
		return "ENVISION:null";
	}
	
	@Override
	public EnvisionObject copy() {
		return this;
	}
	
	@Override
	public Object convertToJavaObject() {
		return null;
	}
	
}
