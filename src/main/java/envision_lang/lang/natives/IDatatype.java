package envision_lang.lang.natives;

import java.lang.reflect.Method;
import java.util.Collection;

import envision_lang.lang.EnvisionObject;
import eutil.datatypes.util.JavaDatatype;

/**
 * An over-arching wrapper to effectively group the types of Primitives and
 * EnvisionDatatypes.
 * 
 * @author Hunter Bragg
 */
public interface IDatatype {
	
	//-----------------------
	// Non-Default Functions
	//-----------------------
	
	/**
	 * Returns the underlying primitive type of this datatype.
	 * <p>
	 * Note: If this datatype is not a primitive, null is returned
	 * instead.
	 * 
	 * @return the underlying primitive type
	 */
	public Primitives getPrimitive();
	
	/**
	 * Wraps this primitive type within an EnvisionDatatype.
	 * 
	 * @return EnvisionDatatype
	 */
	public EnvisionDatatype toDatatype();

	/**
	 * Returns the String representation of this datatype.
	 * 
	 * @return String
	 */
	public String getStringValue();
	
	//-------------------
	// Default Functions
	//-------------------
	
	/**
	 * Returns true if this dataType is also a primitive datatype.
	 * 
	 * @see Primitives
	 * @return true if primitive
	 */
	public default boolean isPrimitive() {
		return getPrimitive() != null;
	}
	
	/**
	 * Returns true if this type is an array type.
	 * 
	 * Most primitive Envision types have a varargs version
	 * of the same type to allow for an unspecified number
	 * of arguments of the same type to be passed to a function
	 * at once.
	 * 
	 * @return true if varargs type
	 */
	public default boolean isArrayType() {
		return Primitives.isArrayType(getPrimitive());
	}
	
	/**
	 * Returns true if this type is a var.
	 * 
	 * The 'var' datatype effectively represents a slot that can hold any
	 * value regardless of the datatype given to it. Furthermore, unless
	 * the 'var' variable is declared to be strong, the value stored could
	 * change to any other datatype at any time.
	 * 
	 * @return true if var type
	 */
	public default boolean isVar() {
		return getPrimitive() == Primitives.VAR;
	}
	
	/**
	 * Returns true if this type is null.
	 * <p>
	 * Note: Null in Envision:Java IS NOT directly equivalent to Java's
	 * Null and should not be interpreted as such. For example, an Object
	 * may be 'null' in Java which would result in this method returning
	 * 'false' due to the fact that Envision:Null and Java:Null are not
	 * the same thing. Envision:Null is essentially a 'placeholder' object
	 * used to convey the fact that something is 'null'.
	 * 
	 * @return true if this object is equivalent to Envision:Null
	 */
	public default boolean isNull() {
		return getPrimitive() == Primitives.NULL;
	}
	
	/**
	 * Returns true if this type is a function.
	 * 
	 * @return true if a function
	 */
	public default boolean isFunction() {
		return getPrimitive() == Primitives.FUNCTION;
	}
	
	/**
	 * Returns true if this type is a field.
	 * 
	 * @return true if a field
	 */
	public default boolean isField() {
		var p = getPrimitive();
		return p != null && p.isField();
	}
	
	/**
	 * Returns true if this type is either a class or a class instance.
	 * <p>
	 * Effectively, any non-primitive type 'could' be a user-defined
	 * class type. As such, this method will also check if this type is
	 * the primitive types of either 'CLASS' or 'CLASS_INSTANCE'.
	 * 
	 * @return true if a class or class instance
	 */
	public default boolean isClass() {
		var p = getPrimitive();
		return (p == null || p == Primitives.CLASS || p == Primitives.CLASS_INSTANCE);
	}
	
	/**
	 * Returns true if this type is a number.
	 * 
	 * @return true if a number
	 */
	public default boolean isNumber() {
		var p = getPrimitive();
		if (p == null) return false;
		return switch (p) {
		case INT, DOUBLE, NUMBER -> true;
		default -> false;
		};
	}
	
	/**
	 * Returns true if this type is a string.
	 * 
	 * @return true if a string
	 */
	public default boolean isString() {
		return getPrimitive() == Primitives.STRING;
	}
	
	/**
	 * Returns true if this type is void.
	 * 
	 * @return true if void
	 */
	public default boolean isVoid() {
		return getPrimitive() == Primitives.VOID;
	}
	
	/**
	 * Returns true if this type is a package.
	 * 
	 * @return true if package
	 */
	public default boolean isPackage() {
		return getPrimitive() == Primitives.PACKAGE;
	}
	
	/**
	 * Returns true if this datatype is specifically a primitive
	 * variable datatype.
	 * 
	 * I.E. int, double, string, char, etc.
	 * 
	 * @see Primitives
	 * @return true if primitive variable type
	 */
	public default boolean isNativePrimitiveType() {
		var p = getPrimitive();
		return p != null && p.isNativeType();
	}
	
	/**
	 * Returns true if this datatype matches the given datatype.
	 * 
	 * @param typeIn The type to be compared
	 * @return true if the types match
	 */
	public default boolean compare(IDatatype typeIn) {
		//null always is false
		if (typeIn == null) return false;
		
		//don't allow mixing of primitives and user-defined types
		if (typeIn.isPrimitive() && !this.isPrimitive()) return false;
		if (!typeIn.isPrimitive() && this.isPrimitive()) return false;
		
		//check primitives
		if (typeIn.isPrimitive() && this.isPrimitive()) {
			return typeIn.getPrimitive() == this.getPrimitive();
		}
		
		//check string type
		return typeIn.getStringValue().equals(this.getStringValue());
	}
	
	//--------
	// Static
	//--------
	
	public static boolean isNumber(IDatatype type) { 
		return type != null && type.isNumber();
	}
	
	/**
	 * Uses instanceof checking to dynamically determine the specific
	 * datatype of the given object.
	 * <p>
	 * Note: This method is generally slow as it has to potentially
	 * check for every single type equivalence in both Envision and Java.
	 * 
	 * @param obj The object to be checked
	 * @return The dynamically determined datatype of the given object
	 */
	public static IDatatype dynamicallyDetermineType(Object obj) {
		if (obj instanceof EnvisionObject o) return o.getDatatype();
		Primitives type = Primitives.getPrimitiveType(obj);
		return NativeTypeManager.datatypeOf(type);
	}
	
	public static IDatatype of(String typeName) {
		if (typeName == null) return EnvisionStaticTypes.NULL_TYPE;
		
		String lower = typeName.toLowerCase();
		IDatatype parsedType = null;
		
		parsedType = switch (lower) {
		case "var" -> EnvisionStaticTypes.VAR_TYPE;
		case "null" -> EnvisionStaticTypes.NULL_TYPE;
		case "void" -> EnvisionStaticTypes.VOID_TYPE;
		case "boolean" -> EnvisionStaticTypes.BOOL_TYPE;
		case "char", "character" -> EnvisionStaticTypes.CHAR_TYPE;
		case "byte", "short", "int", "integer", "long" -> EnvisionStaticTypes.INT_TYPE;
		case "float", "double", "number" -> EnvisionStaticTypes.DOUBLE_TYPE;
		case "string" -> EnvisionStaticTypes.STRING_TYPE;
		case "list" -> EnvisionStaticTypes.LIST_TYPE;
		case "tuple" -> EnvisionStaticTypes.TUPLE_TYPE;
		case "class" -> EnvisionStaticTypes.CLASS_TYPE;
		case "func", "function" -> EnvisionStaticTypes.FUNC_TYPE;
		case "execption" -> EnvisionStaticTypes.EXCEPTION_TYPE;
		case "package" -> EnvisionStaticTypes.PACKAGE_TYPE;
		case "codefile" -> EnvisionStaticTypes.CODE_FILE;
		default -> null;
		};
		
		if (parsedType == null) return new EnvisionDatatype(typeName);
		return parsedType;
	}
	
	/**
	 * Attempts to parse a valid Envision Datatype from the given Java Datatype.
	 * <p>
	 * Note: only a handful of Java types can be directly mapped to Envision types.
	 * Java types that do not have a valid Envision mapping will return Java::NULL.
	 * 
	 * @param type The Java type
	 * @return The mapped Envision type or Java::NULL if not valid
	 */
	public static IDatatype fromJavaType(JavaDatatype type) {
		return switch (type) {
		case NULL -> EnvisionStaticTypes.NULL_TYPE;
		case OBJECT -> EnvisionStaticTypes.VAR_TYPE;
		case BOOLEAN -> EnvisionStaticTypes.BOOL_TYPE;
		case CHAR -> EnvisionStaticTypes.CHAR_TYPE;
		case BYTE, SHORT, INT, LONG -> EnvisionStaticTypes.INT_TYPE;
		case FLOAT, DOUBLE -> EnvisionStaticTypes.DOUBLE_TYPE;
		case STRING -> EnvisionStaticTypes.STRING_TYPE;
		case NUMBER -> EnvisionStaticTypes.NUMBER_TYPE;
		case ARRAY -> EnvisionStaticTypes.LIST_TYPE;
		case CLASS -> EnvisionStaticTypes.CLASS_TYPE;
		default -> null;
		};
	}
	
    /**
     * Attempts to parse a valid Envision Datatype from the given Java class.
     * <p>
     * Note: only a handful of Java types can be directly mapped to Envision types.
     * Java types that do not have a valid Envision mapping will return Java::NULL.
     * 
     * @param type The Java class
     * @return The mapped Envision type or Java::NULL if not valid
     */
	public static IDatatype fromJavaClass(Class<?> type) {
	    if (type == null) return Primitives.NULL;
	    if (type == Object.class) return Primitives.VAR;
	    if (type == Enum.class) return Primitives.ENUM;
	    if (type == Method.class) return Primitives.FUNCTION;
	    if (type == Void.class) return Primitives.VOID;
	    if (type == String.class) return Primitives.STRING;
	    if (Collection.class.isAssignableFrom(type)) return Primitives.LIST;
	    if (type == Number.class || type.isAssignableFrom(Number.class)) return Primitives.NUMBER;
	    if (type == Boolean.class || type == boolean.class) return Primitives.BOOLEAN;
	    if (type == Character.class || type == char.class) return Primitives.CHAR;
	    if (type == Byte.class || type == byte.class) return Primitives.INT;
	    if (type == Short.class || type == short.class) return Primitives.INT;
	    if (type == Integer.class || type == int.class) return Primitives.INT;
	    if (type == Long.class || type == long.class) return Primitives.INT;
	    if (type == Float.class || type == float.class) return Primitives.DOUBLE;
	    if (type == Double.class || type == double.class) return Primitives.DOUBLE;
	    
	    if (type.isArray()) {
	        return Primitives.LIST;
//	        Class<?> arrType = type.getComponentType();
//	        
//	        if (arrType == Object.class) return Primitives.VAR_A;
//	        if (arrType == String.class) return Primitives.STRING_A;
//	        if (arrType == Number.class || arrType.isAssignableFrom(Number.class)) return Primitives.NUMBER_A;
//	        if (arrType == Boolean.class || arrType == boolean.class) return Primitives.BOOLEAN_A;
//	        if (arrType == Character.class || arrType == char.class) return Primitives.CHAR_A;
//	        if (arrType == Byte.class || arrType == byte.class) return Primitives.INT_A;
//	        if (arrType == Short.class || arrType == short.class) return Primitives.INT_A;
//	        if (arrType == Integer.class || arrType == int.class) return Primitives.INT_A;
//	        if (arrType == Long.class || arrType == long.class) return Primitives.INT_A;
//	        if (arrType == Float.class || arrType == float.class) return Primitives.DOUBLE_A;
//	        if (arrType == Double.class || arrType == double.class) return Primitives.DOUBLE_A;
	    }
	    
	    return new EnvisionDatatype(type.getSimpleName());
	}
	
}
