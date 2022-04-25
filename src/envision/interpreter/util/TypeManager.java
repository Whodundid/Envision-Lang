package envision.interpreter.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import envision.exceptions.EnvisionError;
import envision.exceptions.EnvisionWarning;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionBooleanClass;
import envision.lang.datatypes.EnvisionCharClass;
import envision.lang.datatypes.EnvisionDoubleClass;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.datatypes.EnvisionListClass;
import envision.lang.datatypes.EnvisionNumberClass;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;

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
		
		types.put(EnvisionDatatype.BOOL_TYPE.getType(), EnvisionBooleanClass.BOOLEAN_CLASS);
		types.put(EnvisionDatatype.INT_TYPE.getType(), EnvisionIntClass.INT_CLASS);
		types.put(EnvisionDatatype.DOUBLE_TYPE.getType(), EnvisionDoubleClass.DOUBLE_CLASS);
		types.put(EnvisionDatatype.CHAR_TYPE.getType(), EnvisionCharClass.CHAR_CLASS);
		types.put(EnvisionDatatype.STRING_TYPE.getType(), EnvisionStringClass.STRING_CLASS);
		types.put(EnvisionDatatype.LIST_TYPE.getType(), EnvisionListClass.LIST_CLASS);
		types.put(EnvisionDatatype.NUMBER_TYPE.getType(), EnvisionNumberClass.NUMBER_CLASS);
		
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
	
	/*
	 * Returns the EnvisionClass associated with the given datatype.
	 */
	public static EnvisionClass getPrimitiveClass(EnvisionDatatype type) {
		Primitives pType = type.getPrimitiveType();
		return switch (pType) {
		case BOOLEAN -> EnvisionBooleanClass.BOOLEAN_CLASS;
		case CHAR -> EnvisionCharClass.CHAR_CLASS;
		case INT -> EnvisionIntClass.INT_CLASS;
		case DOUBLE -> EnvisionDoubleClass.DOUBLE_CLASS;
		case NUMBER -> EnvisionNumberClass.NUMBER_CLASS;
		case STRING -> EnvisionStringClass.STRING_CLASS;
		case LIST -> EnvisionListClass.LIST_CLASS;
		default -> null;
		};
	}
	
}
