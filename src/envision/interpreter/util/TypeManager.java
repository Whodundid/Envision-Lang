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
import envision.lang.natives.IDatatype;
import envision.lang.natives.NativeTypeManager;
import envision.lang.natives.Primitives;
import envision.lang.util.StaticTypes;

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
		
		types.put(StaticTypes.BOOL_TYPE.getType(), EnvisionBooleanClass.BOOLEAN_CLASS);
		types.put(StaticTypes.INT_TYPE.getType(), EnvisionIntClass.INT_CLASS);
		types.put(StaticTypes.DOUBLE_TYPE.getType(), EnvisionDoubleClass.DOUBLE_CLASS);
		types.put(StaticTypes.CHAR_TYPE.getType(), EnvisionCharClass.CHAR_CLASS);
		types.put(StaticTypes.STRING_TYPE.getType(), EnvisionStringClass.STRING_CLASS);
		types.put(StaticTypes.LIST_TYPE.getType(), EnvisionListClass.LIST_CLASS);
		types.put(StaticTypes.NUMBER_TYPE.getType(), EnvisionNumberClass.NUMBER_CLASS);
		
	}
	
	/**
	 * Defines a new user-defined datatype within this TypeManager.
	 * The user-defined type must include the EnvisionClass to
	 * associate with the type.
	 * 
	 * @param typeIn
	 * @param objectType
	 */
	public void defineType(IDatatype typeIn, EnvisionClass objectType) {
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
	public boolean isTypeDefined(IDatatype typeIn) {
		if (typeIn != null && typeIn.isPrimitive()) return true;
		return types.containsKey(typeIn.getType());
	}
	
	/**
	 * Returned the associated EnvisionClass for the given
	 * user-defined type.
	 * If the type is not defined, null is returned instead.
	 * 
	 * @param typeIn the type to grab
	 * @return the EnvisionClass of the user-defined type
	 */
	public EnvisionClass getTypeClass(IDatatype typeIn) {
		return types.get(typeIn.getType());
	}
	
	/**
	 * Returned the associated EnvisionClass for the given
	 * user-defined type.
	 * If the type is not defined, null is returned instead.
	 * 
	 * @param typeIn the type to grab
	 * @return the EnvisionClass of the user-defined type
	 */
	public EnvisionClass getTypeClass(String typeIn) {
		return types.get(typeIn);
	}
	
	/**
	 * Returns a hashset of all user defined datatypes within this
	 * TypeManager.
	 * 
	 * @return a set of all types
	 */
	public HashSet<IDatatype> getTypes() {
		var mapped_types = types.keySet().stream().map(t -> NativeTypeManager.datatypeOf(t)).collect(Collectors.toList());
		return new HashSet<IDatatype>(mapped_types);
	}
	
	/*
	 * Returns the EnvisionClass associated with the given datatype.
	 */
	public static EnvisionClass getPrimitiveClass(IDatatype type) {
		Primitives pType = type.getPrimitive();
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
