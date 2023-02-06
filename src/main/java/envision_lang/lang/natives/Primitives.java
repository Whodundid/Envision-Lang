package envision_lang.lang.natives;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.packages.EnvisionLangPackage;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.datatypes.util.JavaDatatype;

/**
 * An enum outlining all primitive datatypes to be associated with the
 * Envision:Java scripting language. Furthermore, all user created
 * class types are effectively required to be directly mapped to one
 * of these underlying primitive types.
 * <p>
 * Primitive types are used in conjunction with the EnvisionDataType
 * object for encapsulation purposes while being associated with any
 * specific object created within the Envision scripting language.
 * <p>
 * Certain primitive types can be array types which are used to pass an
 * unspecified number of arguments to functions and are represented
 * with the variable arguments keyword '...' or varargs for short.
 * 
 * @author Hunter Bragg
 */
public enum Primitives implements IDatatype {
	
	// native language types
	
	VOID("//void"),
	NULL("//_null_"),
	CLASS("//class"),
	CLASS_INSTANCE("//class_inst"),
	INTERFACE("//interface"),
	PACKAGE("//package"),
	CODE_FILE("//code_file"),
	FUNCTION("//function"),
	//OPERATOR("operator"),
	//EXCEPTION("exception"),
	
	// basic types
	
	BOOLEAN("//boolean"),
	CHAR("//char"),
	INT("//int"),
	DOUBLE("//double"),
	NUMBER("//number"),
	STRING("//string"),
	LIST("//list"),
	TUPLE("//tuple"),
	
	// dynamic object types
	
	VAR("//var_type"),
	ENUM("//enum"),
	ENUM_TYPE("//enum_type"),
	
	// array types
	
	BOOLEAN_A("//[boolean", BOOLEAN),
	CHAR_A("//[char", CHAR),
	INT_A("//[int", INT),
	DOUBLE_A("//[double", DOUBLE),
	STRING_A("//[string", STRING),
	NUMBER_A("//[number", NUMBER),
	VAR_A("//[var", VAR),
	
	;
	
	/** The String value of the primitive type. */
	public final String string_value;
	/** True if this primitive type is a special type used for varargs type passing. */
	public final boolean is_array_type;
	/** The non-array type of this varargs type. NOTE: this will be null unless this is an array type. */
	private final Primitives non_array_type;
	
	//==============
	// Constructors
	//==============
	
	private Primitives(String valueIn) {
		string_value = valueIn;
		is_array_type = false;
		non_array_type = null;
	}
	
	private Primitives(String valueIn, Primitives non_array_type_in) {
		string_value = valueIn;
		is_array_type = true;
		non_array_type = non_array_type_in;
	}
	
	//---------------------------------------
	
	/** The internal mapping of all known primitive type values. */
	private static final EList<Primitives> primitivesList = new EArrayList<>();
	
	/** Maps string representations of primitives directly to its actual primitive type. */
	private static final Map<String, Primitives> stringValueToPrimitiveMap = new HashMap<>();
	/** Maps envision IKeyword types directly to primitive types. */
	private static final Map<IKeyword, Primitives> keywordToPrimitiveMap = new HashMap<>();
	/** Maps Java datatypes directly to an envision datatype (if applicable). */
	private static final Map<JavaDatatype, Primitives> javaDatatypeToPrimitiveMap = new HashMap<>();
	
	/** A set of all primitives that can have parameters. */
	private static final Set<Primitives> primitivesThatCanBeParameterized = new HashSet<>();
	/** A set of all primitives that can have generics. */
	private static final Set<Primitives> primitivesThatCanHaveGenerics = new HashSet<>();
	
	/** The set of all primitives that are numbers in envision. */
	private static final Set<Primitives> numberPrimitives = new HashSet<>();
	/** The set of all primitive types that could be used as a class field in envision. */
	private static final Set<Primitives> fieldPrimitives = new HashSet<>();
	/** The set of all primitive types that are natively passed by value in envision. */
	private static final Set<Primitives> passByValuePrimitives = new HashSet<>();
	
	//=============
	// Static init
	//=============
	
	static {
		for (Primitives p : values()) {
			primitivesList.add(p);
			stringValueToPrimitiveMap.put(p.string_value, p);
		}
		
		stringValueToPrimitiveMap.put(ReservedWord.VAR.typeString, VAR);
		stringValueToPrimitiveMap.put(ReservedWord.VOID.typeString, VOID);
		stringValueToPrimitiveMap.put(ReservedWord.NULL.typeString, NULL);
		stringValueToPrimitiveMap.put(ReservedWord.BOOLEAN.typeString, BOOLEAN);
		stringValueToPrimitiveMap.put(ReservedWord.NUMBER.typeString, NUMBER);
		stringValueToPrimitiveMap.put(ReservedWord.INT.typeString, INT);
		stringValueToPrimitiveMap.put(ReservedWord.DOUBLE.typeString, DOUBLE);
		stringValueToPrimitiveMap.put(ReservedWord.CHAR.typeString, CHAR);
		stringValueToPrimitiveMap.put(ReservedWord.STRING.typeString, STRING);
		stringValueToPrimitiveMap.put(ReservedWord.ENUM.typeString, ENUM);
		stringValueToPrimitiveMap.put(ReservedWord.LIST.typeString, LIST);
		stringValueToPrimitiveMap.put(ReservedWord.TUPLE.typeString, TUPLE);
		
		keywordToPrimitiveMap.put(ReservedWord.TRUE, BOOLEAN);
		keywordToPrimitiveMap.put(ReservedWord.FALSE, BOOLEAN);
		keywordToPrimitiveMap.put(ReservedWord.VOID, VOID);
		keywordToPrimitiveMap.put(ReservedWord.NULL, NULL);
		keywordToPrimitiveMap.put(ReservedWord.CLASS, CLASS);
//		keywordToPrimitiveMap.put(ReservedWord.INTERFACE, INTERFACE);
//		keywordToPrimitiveMap.put(ReservedWord.PACKAGE, PACKAGE);
		keywordToPrimitiveMap.put(ReservedWord.FUNC, FUNCTION);
		keywordToPrimitiveMap.put(ReservedWord.BOOLEAN, BOOLEAN);
		keywordToPrimitiveMap.put(ReservedWord.CHAR, CHAR);
		keywordToPrimitiveMap.put(ReservedWord.INT, INT);
		keywordToPrimitiveMap.put(ReservedWord.DOUBLE, DOUBLE);
		keywordToPrimitiveMap.put(ReservedWord.STRING, STRING);
		keywordToPrimitiveMap.put(ReservedWord.NUMBER, NUMBER);
		keywordToPrimitiveMap.put(ReservedWord.LIST, LIST);
		keywordToPrimitiveMap.put(ReservedWord.TUPLE, TUPLE);
		keywordToPrimitiveMap.put(ReservedWord.VAR, VAR);
		keywordToPrimitiveMap.put(ReservedWord.ENUM, ENUM);
		keywordToPrimitiveMap.put(ReservedWord.CHAR_LITERAL, CHAR);
		keywordToPrimitiveMap.put(ReservedWord.INT_LITERAL, INT);
		keywordToPrimitiveMap.put(ReservedWord.DOUBLE_LITERAL, DOUBLE);
		keywordToPrimitiveMap.put(ReservedWord.STRING_LITERAL, STRING);
		
		javaDatatypeToPrimitiveMap.put(JavaDatatype.BOOLEAN, BOOLEAN);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.CHAR, CHAR);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.BYTE, INT);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.SHORT, INT);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.INT, INT);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.LONG, INT);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.FLOAT, DOUBLE);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.DOUBLE, DOUBLE);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.STRING, STRING);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.NUMBER, NUMBER);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.OBJECT, VAR);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.ARRAY, LIST);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.INTERFACE, INTERFACE);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.CONSTRUCTOR, FUNCTION);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.METHOD, FUNCTION);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.CLASS, CLASS);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.ENUM, ENUM);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.VOID, VOID);
		javaDatatypeToPrimitiveMap.put(JavaDatatype.NULL, NULL);
		
		primitivesThatCanBeParameterized.add(LIST);
		primitivesThatCanBeParameterized.add(FUNCTION);
		primitivesThatCanBeParameterized.add(CLASS);
		
		primitivesThatCanHaveGenerics.add(FUNCTION);
		primitivesThatCanHaveGenerics.add(CLASS);
		
		numberPrimitives.add(INT);
		numberPrimitives.add(DOUBLE);
		numberPrimitives.add(NUMBER);
		
		fieldPrimitives.add(BOOLEAN);
		fieldPrimitives.add(CHAR);
		fieldPrimitives.add(INT);
		fieldPrimitives.add(DOUBLE);
		fieldPrimitives.add(NUMBER);
		fieldPrimitives.add(STRING);
		fieldPrimitives.add(LIST);
		fieldPrimitives.add(TUPLE);
		fieldPrimitives.add(VAR);
		fieldPrimitives.add(CLASS);
		fieldPrimitives.add(CODE_FILE);
		fieldPrimitives.add(FUNCTION);
		fieldPrimitives.add(ENUM);
		fieldPrimitives.add(CLASS_INSTANCE);
		//fieldPrimitives.add(INTERFACE);
		//fieldPrimitives.add(PACKAGE);
		
		passByValuePrimitives.add(BOOLEAN);
		passByValuePrimitives.add(CHAR);
		passByValuePrimitives.add(INT);
		passByValuePrimitives.add(DOUBLE);
		passByValuePrimitives.add(NUMBER);
		passByValuePrimitives.add(STRING);
		//passByValuePrimitives.add(LIST);
		//passByValuePrimitives.add(TUPLE);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public Primitives getPrimitive() { return this; }
	@Override public EnvisionDatatype toDatatype() { return NativeTypeManager.datatypeOf(this); }
	@Override public String getStringValue() { return string_value; }
	
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
	
	public boolean isVarType() {
		return this == VAR;
	}
	
	/**
	 * Returns true if this type is passed-by-value.
	 * I.E. boolean, int, double, etc.
	 * 
	 * @return true if this primitive follows pass-by-value rules
	 */
	public boolean isPassByValue() {
		return passByValuePrimitives.contains(this);
	}
	
	/**
	 * Returns true if this type is passed-by-reference.
	 * I.E. list, tuple, function, etc.
	 * 
	 * @return true if this primitive follows pass-by-reference rules
	 */
	public boolean isPassByReference() {
		return !isPassByValue();
	}
	
	/**
	 * Returns true if this primitive type is a number type.
	 * I.E. int, double, number.
	 */
	public boolean isNumber() {
		return numberPrimitives.contains(this);
	}
	
	/**
	 * Returns true if this primitive type could be used as a field.
	 */
	public boolean isField() {
		return fieldPrimitives.contains(this);
	}
	
	/**
	 * Returns true if this primitive type represents a function.
	 */
	public boolean isFunction() {
		return this == FUNCTION;
	}
	
	/**
	 * Returns true if this datatype is a variable argument type.
	 */
	public boolean isArrayType() {
		return is_array_type;
	}
	
	/**
	 * If this type is a varags type, return the non varags type.
	 */
	public Primitives getNonArrayType() {
		return non_array_type;
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
		return (t != null) ? t.is_array_type : false;
	}
	
	public static boolean isNumber(Primitives t) {
		return numberPrimitives.contains(t);
	}
	
	public static boolean isNumber(String typeIn) {
		return isNumber(getPrimitiveType(typeIn.toLowerCase()));
	}
	
	public static boolean canBeParameterized(Primitives typeIn) {
		return primitivesThatCanBeParameterized.contains(typeIn);
	}
	
	public static boolean canHaveGenerics(Primitives typeIn) {
		return primitivesThatCanHaveGenerics.contains(typeIn);
	}
	
	public static Primitives getPrimitiveType(Token token) {
		return getPrimitiveType(token.getKeyword());
	}
	
	public static Primitives getPrimitiveType(IKeyword keyword) {
		if (keyword.isOperator()) return null;
		return keywordToPrimitiveMap.getOrDefault(keyword, null);
	}
	
	public static Primitives getPrimitiveType(String typeIn) {
		if (typeIn == null) return null;
		return stringValueToPrimitiveMap.getOrDefault(typeIn, null);
	}
	
	public static Primitives getPrimitiveType(JavaDatatype typeIn) {
		if (typeIn == null) return null;
		return javaDatatypeToPrimitiveMap.getOrDefault(typeIn, null);
	}
	
	public static Primitives getPrimitiveType(EnvisionObject obj) {
		if (obj instanceof EnvisionVariable env_var) return env_var.getPrimitiveType();
		if (obj instanceof EnvisionList) return LIST;
		if (obj instanceof EnvisionFunction) return FUNCTION;
		if (obj instanceof EnvisionClass) return CLASS;
		//if (obj instanceof ClassInstance env_inst) return CLASS_INSTANCE;
		//if (obj instanceof EnvisionEnum env_enum) return ENUM;
		//if (obj instanceof EnumValue env_enum_val) return ENUM_TYPE;
		if (obj instanceof EnvisionCodeFile) return CODE_FILE;
		if (obj instanceof EnvisionLangPackage) return PACKAGE;
		return (obj != null) ? obj.getDatatype().getPrimitive() : null;
	}
	
	public static Primitives getPrimitiveType(Object in) {
		//if (in instanceof IKeyword k && k.isOperator()) return OPERATOR;
		if (in instanceof EnvisionObject env_obj) return getPrimitiveType(env_obj);
		return getPrimitiveType(JavaDatatype.getDataType(in));
	}
	
	public static Primitives getNumberType(String in) {
		return getPrimitiveType(JavaDatatype.getNumberType(in));
	}
	
	public static Primitives getNumberType(Number in) {
		return getPrimitiveType(JavaDatatype.getNumberType(in));
	}
	
	public static String getTypeString(Object in) {
		Primitives t = getPrimitiveType(in);
		return (t != null) ? t.string_value : NULL.string_value;
	}
	
	/**
	 * Returns true if this datatype can be assigned from the specified
	 * type.
	 */
//	public boolean canBeAssignedFrom(Primitives type) {
//		return switch (this) {
//		//these are wildcard values so they should be able to take on any type and any value
//		case VAR -> true;
//		//these types can only ever be assigned by their respective type
//		case ENUM -> this == ENUM;
//		case FUNCTION -> this == FUNCTION;
//		case CLASS -> this == CLASS;
//		//case EXCEPTION: return this == EXCEPTION;
//		//these can never be assigned
//		case VOID, NULL -> false;
//		//variable types
//		case BOOLEAN -> type == BOOLEAN;
//		case CHAR -> type == CHAR;
//		case INT -> type == NUMBER || type == INT;
//		case DOUBLE -> type == NUMBER || type == INT || type == DOUBLE;
//		case STRING -> type == CHAR || type == STRING;
//		case NUMBER -> type == NUMBER || type == INT || type == DOUBLE || type == CHAR;
//		//assume false by default
//		default -> false;
//		};
//	}
	
}
