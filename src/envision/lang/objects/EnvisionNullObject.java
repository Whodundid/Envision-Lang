package envision.lang.objects;

import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.NullVariableError;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;

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
public class EnvisionNullObject extends EnvisionVariable {
	
	/**
	 * The static datatype representing null objects. Use throughout.
	 */
	public static final EnvisionDatatype NULL_TYPE = Primitives.NULL.toDatatype();
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Creates a new 'null' object.
	 */
	public EnvisionNullObject() {
		super(NULL_TYPE);
		hasInternalFunctions = false;
	}
	
	//------------------------------------
	// Overriding standard object methods
	//------------------------------------
	
	@Override
	public EnvisionDatatype getDatatype() {
		return NULL_TYPE;
	}
	
	@Override
	public String toString() {
		return "ENVISION:null";
	}
	
	@Override
	public EnvisionVariable set(Object valIn) throws FinalVarReassignmentError {
		throw new NullVariableError();
	}
	
}
