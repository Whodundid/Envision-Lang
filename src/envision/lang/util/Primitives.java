package envision.lang.util;

import java.util.HashMap;

import envision.EnvisionCodeFile;
import envision.lang.EnvisionObject;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.internal.EnvisionFunction;
import envision.packages.EnvisionPackage;
import envision.tokenizer.ReservedWord;
import envision.tokenizer.Token;
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
public enum Primitives {
	
	// required language types
	
	VOID("void"),
	NULL("_null_"),
	CLASS("//class"),
	CLASS_INSTANCE("//class_inst"),
	INTERFACE("//interface"),
	PACKAGE("package"),
	CODE_FILE("code_file"),
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
	
	VAR("var"),
	ENUM("enum"),
	ENUM_TYPE("//enum_type"),
	
	// vararg types
	
	BOOLEAN_A("[boolean"),
	CHAR_A("[char"),
	INT_A("[int"),
	DOUBLE_A("[double"),
	STRING_A("[string"),
	NUMBER_A("[number"),
	LIST_A("[list"),
	VAR_A("[var"),
	
	;
	
	/**
	 * The String version of the primitive type.
	 */
	public final String string_type;
	
	/**
	 * Stores every primitive type for fast O(1) type retrieval time.
	 */
	private static final HashMap<String, Primitives> mappedTypes = new HashMap();
	
	//--------------
	// Constructors
	//--------------
	
	private Primitives(String typeIn) {
		string_type = typeIn;
	}
	
	//---------------------------------------
	
	static {
		for (var d : values()) mappedTypes.put(d.string_type, d);
	}
	
	//---------------------------------------
	
	//---------
	// Methods
	//---------
	
	/**
	 * Wraps this primitive type within an EnvisionDatatype.
	 * 
	 * @return EnvisionDatatype
	 */
	public EnvisionDatatype toDatatype() {
		return new EnvisionDatatype(this);
	}
	
	public boolean compare(EnvisionDatatype typeIn) {
		return (typeIn != null) ? typeIn.getPrimitiveType() == this : false;
	}
	
	/**
	 * @return true if the given primitive type can take on object
	 *         parameters.
	 */
	public boolean canBeParameterized() {
		return canBeParameterized(this);
	}
	
	/**
	 * Returns true if this primitive is an array type (varargs).
	 * @param in
	 * @return
	 */
	public static boolean isArrayType(Primitives t) {
		return (t != null) ? t.isArrayType() : false;
	}
	
	/**
	 * Returns true if this datatype is a varaible argument type.
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
		switch (this) {
		case VAR_A:
			return VAR;
		case BOOLEAN_A:
			return BOOLEAN;
		case CHAR_A:
			return CHAR;
		case INT_A:
			return INT;
		case DOUBLE_A:
			return DOUBLE;
		case STRING_A:
			return STRING;
		case NUMBER_A:
			return NUMBER;
		case LIST_A:
			return LIST;
		default:
			return this;
		}
	}
	
	/**
	 * Returns true if this primitive type is a number type.
	 * I.E. int, double, number.
	 */
	public boolean isNumber() {
		switch (this) {
		case INT:
		case DOUBLE:
		case NUMBER:
			return true;
		default:
			return false;
		}
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
		switch (typeIn) {
		case "var":
			return VAR;
		case "[var":
			return VAR_A;
		case "boolean":
			return BOOLEAN;
		case "[boolean":
			return BOOLEAN_A;
		case "char":
			return CHAR;
		case "[char":
			return CHAR_A;
		case "int":
			return INT;
		case "[int":
			return INT_A;
		case "double":
			return DOUBLE;
		case "[double":
			return DOUBLE_A;
		case "number":
			return NUMBER;
		case "[number":
			return NUMBER_A;
		case "string":
			return STRING;
		case "[string":
			return STRING_A;
		case "list":
			return LIST;
		case "[list":
			return LIST_A;
		case "package":
			return PACKAGE;
		default:
			return null;
		}
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
		return (obj != null) ? obj.getDatatype().getPrimitiveType() : null;	
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
		switch (this) {
		//these are wildcard values so they should be able to take on any type and any value
		case VAR:
			return true;
		//these types can only ever be assigned by their respective type
		case ENUM:
			return this == ENUM;
		case FUNCTION:
			return this == FUNCTION;
		case CLASS:
			return this == CLASS;
		//case EXCEPTION: return this == EXCEPTION;
		//these can never be assigned
		case VOID:
		case NULL:
			return false;
		//variable types
		case BOOLEAN:
			switch (type) {
			case BOOLEAN:
				return true;
			default:
				return false;
			}
		case CHAR:
			switch (type) {
			case CHAR:
				return true;
			default:
				return false;
			}
		case INT:
			switch (type) {
			case NUMBER:
			case INT:
				return true;
			default:
				return false;
			}
		case DOUBLE:
			switch (type) {
			case NUMBER:
			case INT:
			case DOUBLE:
				return true;
			default:
				return false;
			}
		case STRING:
			switch (type) {
			case CHAR:
			case STRING:
				return true;
			default:
				return false;
			}
		case NUMBER:
			switch (type) {
			case NUMBER:
			case INT:
			case DOUBLE:
			case CHAR:
				return true;
			default:
				return false;
			}
			//assume false by default
		default:
			return false;
		}
	}
	
	public boolean isObject() {
		return this == VAR;
	}
	
}
