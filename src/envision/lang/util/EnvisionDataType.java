package envision.lang.util;

import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import eutil.datatypes.util.EDataType;

public enum EnvisionDataType {
	BOOLEAN("boolean"),
	BOOLEAN_A("[boolean"),
	CHAR("char"),
	CHAR_A("[char"),
	INT("int"),
	INT_A("[int"),
	DOUBLE("double"),
	DOUBLE_A("[double"),
	STRING("string"),
	STRING_A("[string"),
	NUMBER("number"),
	NUMBER_A("[number"),
	LIST("list"),
	LIST_A("[list"),
	VOID("void"),
	NULL("_null_"),
	ENUM("enum"),
	ENUM_TYPE("//enum_type"),
	OBJECT("object"),
	OBJECT_A("[object"),
	CLASS("//class"),
	CLASS_INSTANCE("//instance"),
	METHOD("//function"),
	OPERATOR("operator"),
	//EXCEPTION("exception"),
	PACKAGE("package"),
	CODE_FILE("code_file");
	
	public final String type;
	
	private EnvisionDataType(String typeIn) {
		type = typeIn;
	}
	
	//---------------------------------------
	
	public String type() { return type; }
	
	public boolean canBeParameterized() { return canBeParameterized(this); }
	
	public static boolean isArrayType(String in) {
		EnvisionDataType t = getDataType(in);
		return (t != null) ? t.isArrayType() : false;
	}
	
	/** Returns true if this datatype is a varaible argument type. */
	public boolean isArrayType() {
		switch (this) {
		case OBJECT_A:
		case BOOLEAN_A:
		case CHAR_A:
		case INT_A:
		case DOUBLE_A:
		case STRING_A:
		case NUMBER_A:
		case LIST_A: return true;
		default: return false;
		}
	}
	
	/** If this type is a varags type, return the non varags type. */
	public EnvisionDataType getNonArrayType() {
		switch (this) {
		case OBJECT_A: return OBJECT;
		case BOOLEAN_A: return BOOLEAN;
		case CHAR_A: return CHAR;
		case INT_A: return INT;
		case DOUBLE_A: return DOUBLE;
		case STRING_A: return STRING;
		case NUMBER_A: return NUMBER;
		case LIST_A: return LIST;
		default: return this;
		}
	}
	
	public boolean isNumber() {
		switch (this) {
		case INT: case DOUBLE: case NUMBER: return true;
		default: return false;
		}
	}
	
	public static boolean isNumber(EnvisionDataType typeIn) { return typeIn.isNumber(); }
	public static boolean isNumber(String typeIn) { return isNumber(getDataType(typeIn.toLowerCase())); }
	
	public boolean isField() {
		switch (this) {
		case BOOLEAN:
		case CHAR:
		case INT:
		case DOUBLE:
		case STRING:
		case NUMBER:
		case LIST:
		case OBJECT:
		case CLASS_INSTANCE: return true;
		default: return false;
		}
	}
	
	public boolean isMethod() {
		switch (this) {
		case METHOD: return true;
		default: return false;
		}
	}
	
	public static boolean canBeParameterized(EnvisionDataType typeIn) {
		switch (typeIn) {
		case LIST:
		case METHOD:
		case CLASS:
		case OBJECT: return true;
		//case EXCEPTION: return true;
		default: return false;
		}
	}
	
	public static boolean canHaveGenerics(EnvisionDataType typeIn) {
		switch (typeIn) {
		case METHOD:
		case CLASS: return true;
		default: return false;
		}
	}
	
	public static EnvisionDataType getDataType(Token token) {
		return getDataType(token.getKeyword());
	}
	
	public static EnvisionDataType getDataType(String typeIn) {
		if (typeIn == null) return null;
		switch (typeIn) {
		case "object": return OBJECT;
		case "[object": return OBJECT_A;
		case "boolean": return BOOLEAN;
		case "[boolean": return BOOLEAN_A;
		case "char": return CHAR;
		case "[char": return CHAR_A;
		case "int": return INT;
		case "[int": return INT_A;
		case "double": return DOUBLE;
		case "[double": return DOUBLE_A;
		case "number": return NUMBER;
		case "[number": return NUMBER_A;
		case "string": return STRING;
		case "[string": return STRING_A;
		case "list": return LIST;
		case "[list": return LIST_A;
		case "package": return PACKAGE;
		default: return null;
		}
	}
	
	public static EnvisionDataType getDataType(Keyword keyIn) {
		if (keyIn == null) return null;
		switch (keyIn) {
		case OBJECT: return OBJECT;
		case BOOLEAN: return BOOLEAN;
		case CHAR: return CHAR;
		case INT: return INT;
		case DOUBLE: return DOUBLE;
		case STRING: return STRING;
		case NUMBER: return NUMBER;
		//case LIST: return LIST;
		case VOID: return VOID;
		case NULL: return NULL;
		case ENUM: return ENUM;
		//case FUNCTION: return METHOD;
		case CLASS: return CLASS;
		//case EXCEPTION: return EXCEPTION;
		case PACKAGE: return PACKAGE;
		default: return null;
		}
	}
	
	public static EnvisionDataType getDataType(EDataType typeIn) {
		if (typeIn == null) return null;
		switch (typeIn) {
		case VOID: return VOID;
		case OBJECT: return OBJECT;
		case BOOLEAN: return BOOLEAN;
		case CHAR: return CHAR;
		case BYTE:
		case SHORT:
		case INT:
		case LONG: return INT;
		case FLOAT:
		case DOUBLE: return DOUBLE;
		case STRING: return STRING;
		case NUMBER: return NUMBER;
		case CONSTRUCTOR:
		case METHOD: return METHOD;
		case ARRAY: return LIST;
		case CLASS: return CLASS;
		case ENUM: return ENUM;
		case NULL: return NULL;
		default: return null;
		}
	}
	
	public static EnvisionDataType getDataType(Object in) {
		if (in instanceof Keyword && ((Keyword) in).isOperator) return OPERATOR;
		return getDataType(EDataType.getDataType(in));
	}
	
	public static EnvisionDataType getNumberType(String in) {
		return getDataType(EDataType.getNumberType(in));
	}
	
	public static EnvisionDataType getNumberType(Number in) {
		return getDataType(EDataType.getNumberType(in));
	}
	
	public static String getTypeName(Object in) {
		EnvisionDataType t = getDataType(in);
		return (t != null) ? t.type : NULL.type;
	}
	
	/** Returns true if this datatype can be assigned from the specified type. */
	public boolean canBeAssignedFrom(EnvisionDataType type) {
		switch (this) {
		//these are wildcard values so they should be able to take on any type and any value
		case OBJECT:
		//these types can only ever be assigned by their respective type
		case ENUM: return this == ENUM;
		case METHOD: return this == METHOD;
		case CLASS: return this == CLASS;
		//case EXCEPTION: return this == EXCEPTION;
		//these can never be assigned
		case VOID:
		case NULL: return false;
		//variable types
		case BOOLEAN:
			switch (type) {
			case BOOLEAN: return true;
			default: return false;
			}
		case CHAR:
			switch (type) {
			case CHAR: return true;
			default: return false;
			}
		case INT:
			switch (type) {
			case NUMBER: case INT: return true;
			default: return false;
			}
		case DOUBLE:
			switch (type) {
			case NUMBER: case INT: case DOUBLE: return true;
			default: return false;
			}
		case STRING:
			switch (type) {
			case CHAR: case STRING: return true;
			default: return false;
			}
		case NUMBER:
			switch (type) {
			case NUMBER: case INT: case DOUBLE: case CHAR: return true;
			default: return false;
			}
		//assume false by default
		default: return false;
		}
	}

	public boolean isObject() { return this == OBJECT; }
	
}
