package envision.lang.util;

import envision.lang.EnvisionObject;

/**
 * A low level function placeholder that is intended to be handled
 * natively within primitive object types.
 * <p>
 * Due to the fact that a primitive variable type can actually never
 * be extended from, there is no reason to have actual
 * EnvisionFunctions directly backing the internal primitive member
 * functions.
 * <p>
 * This design choice is intended to provide a significant performance
 * optimization by removing the need to create actual primitive member
 * function instances.
 * 
 * @author Hunter Bragg
 */
public class PrimitiveFunctionPrototype extends EnvisionObject {

	/**
	 * The function name that this placeholder will take in scopes.
	 */
	private final String func_name;	
	
	//--------------
	// Constructors
	//--------------
	
	public PrimitiveFunctionPrototype(String nameIn) {
		super(EnvisionDatatype.FUNC_TYPE);
		func_name = nameIn;
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the placeholder function name of this prototype.
	 * 
	 * @return The name of this function
	 */
	public String getFunctionName() { return func_name; }
	
}
