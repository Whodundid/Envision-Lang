package envision_lang.lang.natives;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.packages.EnvisionPackage;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.DataTypeUtil;

/**
 * An enum outlining all primitive datatypes to be associated with the
 * Envision:Java scripting language. Furthermore, all user created
 * class types are effectively required to be directly mapped to one
 * of these underlying primitive types.
 * <p>
 * Primitive types are used in conjunction with the EnvisionDataType
 * object for encapsulation purposes while being associated with any
 * specific object created within the language.
 * <p>
 * Certain primitive types are array types which are used to pass an
 * unspecified number of arguments to functions and are represented
 * with the variable arguments keyword '...' or varargs for short.
 * 
 * @author Hunter Bragg
 */
public enum Primitives implements IDatatype {
	
	// required language types
	
	VOID("void"),
	NULL("//_null_"),
	CLASS("//class"),
	CLASS_INSTANCE("//class_inst"),
	INTERFACE("//interface"),
	PACKAGE("package"),
	CODE_FILE("//code_file"),
	FUNCTION("//function"),
	//OPERATOR("operator"),
	//EXCEPTION("exception"),
	
	// basic types
	
	BOOLEAN("boolean"),
	CHAR("char"),
	INT("int"),
	DOUBLE("double"),
	STRING("string"),
	NUMBER("number"),
	LIST("list"),
	
	// dynamic object types
	
	VAR("//var_type"),
	ENUM("enum"),
	ENUM_TYPE("//enum_type"),
	
	// vararg types
	
	BOOLEAN_A("//[boolean"),
	CHAR_A("//[char"),
	INT_A("//[int"),
	DOUBLE_A("//[double"),
	STRING_A("//[string"),
	NUMBER_A("//[number"),
	LIST_A("//[list"),
	VAR_A("//[var"),
	
	;
	
	/**
	 * The String version of the primitive type.
	 */
	public final String string_type;
	
	//--------------
	// Constructors
	//--------------
	
	private Primitives(String typeIn) {
		string_type = typeIn;
	}
	
	//---------------------------------------
	
	//-----------
	// Overrides
	//-----------
	
	@Override public Primitives getPrimitive() { return this; }
	@Override public EnvisionDatatype toDatatype() { return NativeTypeManager.datatypeOf(string_type); }
	@Override public String getType() { return string_type; }
	
	//---------
	// Methods
	//---------
	
	/**
	 * @return true if the given primitive type can take on object
	 *         parameters.
	 */
	public boolean canBeParameterized() {
		return canBeParameterized(this);
	}
	
	/**
	 * Returns true if this datatype is a variable argument type.
	 */
	public boolean isArrayType() {
		switch (this) {
		case VAR_A:
		case BOOLEAN_A:
		case CHAR_A:
		case INT_A:
		case DOUBLE_A:
		case STRING_A:
		case NUMBER_A:
		case LIST_A:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * If this type is a varags type, return the non varags type.
	 */
	public Primitives getNonArrayType() {
		return switch (this) {
		case VAR_A -> VAR;
		case BOOLEAN_A -> BOOLEAN;
		case CHAR_A -> CHAR;
		case INT_A -> INT;
		case DOUBLE_A -> DOUBLE;
		case STRING_A -> STRING;
		case NUMBER_A -> NUMBER;
		case LIST_A -> LIST;
		default -> this;
		};
	}
	
	/**
	 * Returns true if this primitive type is a number type.
	 * I.E. int, double, number.
	 */
	public boolean isNumber() {
		return switch (this) {
		case INT, DOUBLE, NUMBER -> true;
		default -> false;
		};
	}
	
	/**
	 * Returns true if this primitive type could be used as a field.
	 */
	public boolean isField() {
		switch (this) {
		case BOOLEAN:
		case CHAR:
		case INT:
		case DOUBLE:
		case STRING:
		case NUMBER:
		case LIST:
		case VAR:
		case CLASS_INSTANCE:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Returns true if this type is a primitive variable.
	 * I.E. boolean, int, double, etc.
	 * 
	 * @return true if a variable
	 */
	public boolean isVariableType() {
		switch (this) {
		case BOOLEAN:
		case CHAR:
		case INT:
		case DOUBLE:
		case STRING:
		case NUMBER:
		case LIST:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Returns true if this primitive type represents a function.
	 */
	public boolean isFunction() {
		return this == FUNCTION;
	}
	
	//----------------
	// Static Methods
	//----------------
	
	/**
	 * Returns true if this primitive is an array type (varargs).
	 * @param in
	 * @return
	 */
	public static boolean isArrayType(Primitives t) {
		return (t != null) ? t.isArrayType() : false;
	}
	
	public static boolean isNumber(Primitives typeIn) {
		return typeIn.isNumber();
	}
	
	public static boolean isNumber(String typeIn) {
		return isNumber(getDataType(typeIn.toLowerCase()));
	}
	
	public static boolean canBeParameterized(Primitives typeIn) {
		switch (typeIn) {
		case LIST:
		case FUNCTION:
		case CLASS:
		case VAR:
			return true;
		//case EXCEPTION: return true;
		default:
			return false;
		}
	}
	
	public static boolean canHaveGenerics(Primitives typeIn) {
		switch (typeIn) {
		case FUNCTION:
		case CLASS:
			return true;
		default:
			return false;
		}
	}
	
	public static Primitives getDataType(Token token) {
		return getDataType(token.getKeyword());
	}
	
	public static Primitives getDataType(String typeIn) {
		if (typeIn == null) return null;
		return switch (typeIn) {
		case "var" -> VAR;
		case "[var" -> VAR_A;
		case "boolean" -> BOOLEAN;
		case "[boolean" -> BOOLEAN_A;
		case "char" -> CHAR;
		case "[char" -> CHAR_A;
		case "int" -> INT;
		case "[int" -> INT_A;
		case "double" -> DOUBLE;
		case "[double" -> DOUBLE_A;
		case "number" -> NUMBER;
		case "[number" -> NUMBER_A;
		case "string" -> STRING;
		case "[string" -> STRING_A;
		case "list" -> LIST;
		case "[list" -> LIST_A;
		case "package" -> PACKAGE;
		default -> null;
		};
	}
	
	public static Primitives getDataType(ReservedWord keyIn) {
		return (keyIn == null) ? null : switch (keyIn) {
		case VAR -> VAR;
		case BOOLEAN -> BOOLEAN;
		case CHAR -> CHAR;
		case INT -> INT;
		case DOUBLE -> DOUBLE;
		case STRING -> STRING;
		case NUMBER -> NUMBER;
		case LIST -> LIST;
		case VOID -> VOID;
		case NULL -> NULL;
		case ENUM -> ENUM;
		case FUNC -> FUNCTION;
		case CLASS -> CLASS;
		default -> null;
		};
	}
	
	public static Primitives getDataType(DataTypeUtil typeIn) {
		return (typeIn == null) ? null : switch (typeIn) {
		case VOID -> VOID;
		case OBJECT -> VAR;
		case BOOLEAN -> BOOLEAN;
		case CHAR -> CHAR;
		case BYTE, SHORT, INT, LONG -> INT;
		case FLOAT, DOUBLE -> DOUBLE;
		case STRING -> STRING;
		case NUMBER -> NUMBER;
		case CONSTRUCTOR, METHOD -> FUNCTION;
		case ARRAY -> LIST;
		case CLASS -> CLASS;
		case ENUM -> ENUM;
		case NULL -> NULL;
		default -> null;
		};
	}
	
	public static Primitives getDataType(EnvisionObject obj) {
		if (obj instanceof EnvisionVariable env_var) return env_var.getPrimitiveType();
		if (obj instanceof EnvisionList env_list) return LIST;
		if (obj instanceof EnvisionFunction env_func) return FUNCTION;
		if (obj instanceof EnvisionClass env_class) return CLASS;
		//if (obj instanceof ClassInstance env_inst) return CLASS_INSTANCE;
		//if (obj instanceof EnvisionEnum env_enum) return ENUM;
		//if (obj instanceof EnumValue env_enum_val) return ENUM_TYPE;
		if (obj instanceof EnvisionCodeFile env_code) return CODE_FILE;
		if (obj instanceof EnvisionPackage env_pkg) return PACKAGE;
		return (obj != null) ? obj.getDatatype().getPrimitive() : null;
	}
	
	public static Primitives getDataType(Object in) {
		//if (in instanceof IKeyword k && k.isOperator()) return OPERATOR;
		if (in instanceof EnvisionObject env_obj) return getDataType(env_obj);
		return getDataType(DataTypeUtil.getDataType(in));
	}
	
	public static Primitives getNumberType(String in) {
		return getDataType(DataTypeUtil.getNumberType(in));
	}
	
	public static Primitives getNumberType(Number in) {
		return getDataType(DataTypeUtil.getNumberType(in));
	}
	
	public static String getTypeString(Object in) {
		Primitives t = getDataType(in);
		return (t != null) ? t.string_type : NULL.string_type;
	}
	
	/**
	 * Returns true if this datatype can be assigned from the specified
	 * type.
	 */
	public boolean canBeAssignedFrom(Primitives type) {
		return switch (this) {
		//these are wildcard values so they should be able to take on any type and any value
		case VAR -> true;
		//these types can only ever be assigned by their respective type
		case ENUM -> this == ENUM;
		case FUNCTION -> this == FUNCTION;
		case CLASS -> this == CLASS;
		//case EXCEPTION: return this == EXCEPTION;
		//these can never be assigned
		case VOID, NULL -> false;
		//variable types
		case BOOLEAN -> type == BOOLEAN;
		case CHAR -> type == CHAR;
		case INT -> type == NUMBER || type == INT;
		case DOUBLE -> type == NUMBER || type == INT || type == DOUBLE;
		case STRING -> type == CHAR || type == STRING;
		case NUMBER -> type == NUMBER || type == INT || type == DOUBLE || type == CHAR;
		//assume false by default
		default -> false;
		};
	}
	
	public boolean isObject() {
		return this == VAR;
	}
	
}
