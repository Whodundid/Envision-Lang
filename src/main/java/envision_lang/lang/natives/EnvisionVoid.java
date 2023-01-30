package envision_lang.lang.natives;

import envision_lang.lang.EnvisionObject;

/**
 * A void object cannot be created by any normal means but is used to
 * represent non-present return values from methods.
 * 
 * @author Hunter Bragg
 */
public class EnvisionVoid extends EnvisionObject {
	
	/**
	 * The single, static void object to be used to denote all
	 * non-existent objects. This is not the same as 'null' however, as a
	 * null value indicates the complete concept of 'nothingness' whereas
	 * void indicates there is 'something' but it just so happens to be
	 * nothing.
	 */
	public static final EnvisionVoid VOID = new EnvisionVoid();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Private to prevent outside instantiation.
	 */
	private EnvisionVoid() {
		super(EnvisionStaticTypes.VOID_TYPE);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return "void";
	}
	
}
