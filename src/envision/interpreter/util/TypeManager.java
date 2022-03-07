package envision.interpreter.util;

import envision.exceptions.EnvisionError;
import envision.exceptions.EnvisionWarning;
import envision.lang.classes.EnvisionClass;
import envision.lang.util.EnvisionDatatype;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Interpreter Utility to help keep track of user-defined types.
 * 
 * @author Hunter Bragg
 */
public class TypeManager {
	
	/**
	 * A collection of all user defined datatypes along with
	 * the EnvisionClass associated with them.
	 */
	private HashMap<String, EnvisionClass> types = new HashMap();
	
	public TypeManager() {
		//load primitive types
		//types.put(Primitives.INT.string_type, new EnvisionInt());
	}
	
	/**
	 * Defines a new user-defined datatype within this TypeManager.
	 * The user-defined type must include the EnvisionClass to
	 * associate with the type.
	 * 
	 * @param typeIn
	 * @param objectType
	 */
	public void defineType(EnvisionDatatype typeIn, EnvisionClass objectType) {
		if (objectType == null) throw new EnvisionError("TypeManager: Null user defined type!");
		
		if (types.containsKey(typeIn.getType())) {
			throw new EnvisionWarning("TypeManager: Potentially unwanted type reassignment. '" + typeIn + "'");
		}
			
		types.put(typeIn.getType(), objectType);
	}
	
	/**
	 * Returns true if the given type is currently defined within
	 * this TypeManager.
	 * 
	 * @param typeIn the type to check for
	 * @return true if defined
	 */
	public boolean isTypeDefined(EnvisionDatatype typeIn) {
		if (typeIn != null && typeIn.isPrimitiveType()) return true;
		return types.containsKey(typeIn.getType());
	}
	
	/**
	 * Retured the associated EnvisionClass for the given
	 * user-defined type.
	 * If the type is not defined, null is returned instead.
	 * 
	 * @param typeIn the type to grab
	 * @return the EnvisionClass of the user-defined type
	 */
	public EnvisionClass getTypeClass(EnvisionDatatype typeIn) {
		return types.get(typeIn.getType());
	}
	
	/**
	 * Returns a hashset of all user defined datatypes within this
	 * TypeManager.
	 * 
	 * @return a set of all types
	 */
	public HashSet<EnvisionDatatype> getTypes() {
		var mapped_types = types.keySet().stream().map(t -> new EnvisionDatatype(t)).collect(Collectors.toList());
		return new HashSet<EnvisionDatatype>(mapped_types);
	}
	
}
