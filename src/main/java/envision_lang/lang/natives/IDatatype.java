package envision_lang.lang.natives;

import envision_lang.lang.EnvisionObject;
import eutil.debug.Broken;

/**
 * An over-arching wrapper to effectively group the types of Primitives and
 * EnvisionDatatypes.
 * 
 * @author Hunter Bragg
 */
@Broken
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
		return (p != null) ? p.isField() : false;
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
		if (p == null || p == Primitives.CLASS || p == Primitives.CLASS_INSTANCE) return true;
		return false;
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
	public default boolean isPrimitiveVariableType() {
		var p = getPrimitive();
		return (p != null) ? p.isPassByValue() : false;
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
		return (type != null) ? type.isNumber() : false;
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
		if (typeName == null) return StaticTypes.NULL_TYPE;
		
		String lower = typeName.toLowerCase();
		IDatatype parsedType = null;
		
		parsedType = switch (lower) {
		case "boolean" -> StaticTypes.BOOL_TYPE;
		case "byte", "short", "int", "integer", "long" -> StaticTypes.INT_TYPE;
		case "float", "double", "number" -> StaticTypes.DOUBLE_TYPE;
		case "char", "character" -> StaticTypes.CHAR_TYPE;
		case "string" -> StaticTypes.STRING_TYPE;
		default -> null;
		};
		
		if (parsedType == null) return NativeTypeManager.datatypeOf(typeName);
		return parsedType;
	}
	
}
