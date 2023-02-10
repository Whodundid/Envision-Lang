package envision_lang.interpreter.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionCharClass;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionNumberClass;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.natives.Primitives;
import eutil.debug.Inefficient;

/**
 * Interpreter Utility to help keep track of user-defined types.
 * 
 * @author Hunter Bragg
 */
@Inefficient(reason = "This should probably move to exist in a per-scope"
	                + "basis to account for clases under the same name but"
	                + "defined in different scopes."
	                + ""
	                + "NOTE: This class is still necessary to discern user-defined"
	                + "types for typed-variable-declarations: 'V v = V()'.")
public class UserDefinedTypeManager {
	
	/**
	 * A collection of all user defined datatypes along with
	 * the EnvisionClass associated with them.
	 */
	private Map<String, EnvisionClass> types = new HashMap();
	
	/**
	 * Defines a new user-defined datatype within this TypeManager.
	 * The user-defined type must include the EnvisionClass to
	 * associate with the type.
	 * 
	 * @param typeIn
	 * @param objectType
	 */
	public void defineType(IDatatype typeIn, EnvisionClass objectType) {
		if (objectType == null) throw new EnvisionLangError("TypeManager: Null user defined type!");
		
		if (types.containsKey(typeIn.getStringValue())) {
			return;
			//throw new EnvisionWarning("TypeManager: Potentially unwanted type reassignment. '" + typeIn + "'");
		}
			
		types.put(typeIn.getStringValue(), objectType);
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
		return types.containsKey(typeIn.getStringValue());
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
		if (typeIn.isPrimitive()) return NativeTypeManager.getClassTypeOf(typeIn.getPrimitive());
		return types.get(typeIn.getStringValue());
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
	 * Returns a set of all user defined datatypes within this
	 * TypeManager.
	 * 
	 * @return a set of all types
	 */
	public Set<IDatatype> getTypes() {
		var mapped_types = types.keySet().stream().map(t -> NativeTypeManager.datatypeOf(t)).collect(Collectors.toList());
		return new HashSet<>(mapped_types);
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
