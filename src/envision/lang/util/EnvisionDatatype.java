package envision.lang.util;

import envision.tokenizer.Token;

/**
 * The underlying datatype class to be used within the Envision:Java
 * scripting language.
 * <p>
 * All datatypes are encapsulated by a String to represent the exact
 * type. Furthermore, If a given type is the same as a basic Envision
 * Primitive datatype, then the primitive type will also be grabbed
 * for simplifying future type casts or conversions.
 * 
 * @see Primitives
 * @author Hunter Bragg
 */
public class EnvisionDatatype {
	
	public static final EnvisionDatatype VAR_TYPE 			= 	Primitives.VAR.toDatatype();
	public static final EnvisionDatatype NULL_TYPE			= 	Primitives.NULL.toDatatype();
	public static final EnvisionDatatype VOID_TYPE			= 	Primitives.VOID.toDatatype();
	public static final EnvisionDatatype BOOL_TYPE			= 	Primitives.BOOLEAN.toDatatype();
	public static final EnvisionDatatype CHAR_TYPE 			= 	Primitives.CHAR.toDatatype();
	public static final EnvisionDatatype INT_TYPE 			= 	Primitives.INT.toDatatype();
	public static final EnvisionDatatype DOUBLE_TYPE 		= 	Primitives.DOUBLE.toDatatype();
	public static final EnvisionDatatype NUMBER_TYPE 		= 	Primitives.NUMBER.toDatatype();
	public static final EnvisionDatatype STRING_TYPE 		= 	Primitives.STRING.toDatatype();
	public static final EnvisionDatatype LIST_TYPE 			= 	Primitives.LIST.toDatatype();
	public static final EnvisionDatatype CLASS_TYPE 		= 	Primitives.CLASS.toDatatype();
	public static final EnvisionDatatype INTERFACE_TYPE 	= 	Primitives.INTERFACE.toDatatype();
	public static final EnvisionDatatype FUNC_TYPE 			= 	Primitives.FUNCTION.toDatatype();
	public static final EnvisionDatatype PACKAGE_TYPE		= 	Primitives.PACKAGE.toDatatype();
	
	//--------
	// Fields
	//--------
	
	/**
	 * The underlying string type.
	 */
	private final String type;
	
	/**
	 * The underlying primitive type. Could be null if not primitve.
	 */
	private final Primitives primitive_type;
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionDatatype(Token in) {
		if (in == null) {
			type = Primitives.VAR.string_type;
			primitive_type = Primitives.VAR;
		}
		else {
			type = in.lexeme;
			primitive_type = Primitives.getDataType(in.lexeme);
		}
	}
	
	public EnvisionDatatype(String in) {
		type = in;
		primitive_type = Primitives.getDataType(in);
	}
	
	public EnvisionDatatype(Primitives in) {
		type = in.string_type;
		primitive_type = in;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return type;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof EnvisionDatatype t) return type.equals(t.type);
		return false;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns true if this dataType is also a primitive datatype.
	 * 
	 * @see Primitives
	 * @return true if primitive
	 */
	public boolean isPrimitiveType() {
		return primitive_type != null;
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
	public boolean isPrimitiveVariableType() {
		return (primitive_type != null) ? primitive_type.isVariableType() : false;
	}
	
	/**
	 * Returns true if this datatype matches the given datatype.
	 * 
	 * @param typeIn The type to be compared
	 * @return true if the types match
	 */
	public boolean compare(EnvisionDatatype typeIn) {
		//null always is false
		if (typeIn == null) return false;
		
		//check primitives
		if (typeIn != null && typeIn.isPrimitiveType() && this.isPrimitiveType()) {
			return typeIn.primitive_type == this.primitive_type;
		}
		
		//check string type
		return typeIn.type.equals(type);
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
	public boolean isArrayType() {
		return Primitives.isArrayType(primitive_type);
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
	public boolean isVar() {
		return primitive_type == Primitives.VAR;
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
	public boolean isNull() {
		return primitive_type == Primitives.NULL;
	}
	
	/**
	 * Returns true if this type is a function.
	 * 
	 * @return true if a function
	 */
	public boolean isFunction() {
		return primitive_type == Primitives.FUNCTION;
	}
	
	/**
	 * Returns true if this type is a field.
	 * 
	 * @return true if a field
	 */
	public boolean isField() {
		return (primitive_type != null) ? primitive_type.isField() : false;
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
	public boolean isClass() {
		return (primitive_type == null) ? true : switch (primitive_type) {
		case CLASS, CLASS_INSTANCE -> true;
		default -> false;
		};
	}
	
	/**
	 * Returns true if this type is a number.
	 * 
	 * @return true if a number
	 */
	public boolean isNumber() {
		return (primitive_type == null) ? false : switch (primitive_type) {
		case INT, DOUBLE, NUMBER -> true;
		default -> false;
		};
	}
	
	/**
	 * Returns true if this type is a string.
	 * 
	 * @return true if a string
	 */
	public boolean isString() {
		return primitive_type == Primitives.STRING;
	}
	
	/**
	 * Returns true if this type is void.
	 * 
	 * @return true if void
	 */
	public boolean isVoid() {
		return (primitive_type == null) ? false : primitive_type == Primitives.VOID;
	}
	
	/**
	 * Returns true if this type is a package.
	 * 
	 * @return true if package
	 */
	public boolean isPackage() {
		return (primitive_type == null) ? false : primitive_type == Primitives.PACKAGE;
	}
	
	//---------
	// Getters
	//---------
	
	/**
	 * Returns the underlying String type of this datatype.
	 * 
	 * @return The underlying type String
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns the underlying primitive type of this datatype.
	 * <p>
	 * Note: If this datatype is not a primitive, null is returned
	 * instead.
	 * 
	 * @return the underlying primitive type
	 */
	public Primitives getPrimitiveType() {
		return primitive_type;
	}
	
	//--------
	// Static
	//--------
	
	public static boolean isNumber(EnvisionDatatype type) { 
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
	public static EnvisionDatatype dynamicallyDetermineType(Object obj) {
		Primitives type = Primitives.getDataType(obj);
		return new EnvisionDatatype(type);
	}
	
}
