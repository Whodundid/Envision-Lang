package envision_lang.tokenizer;

import eutil.EUtil;

import java.util.HashMap;

import envision_lang.lang.natives.Primitives;

/**
 *  The complete set of Envision reserved keywords and operators.
 *  <p>
 *  Keyword and operator lookup is performed in a constant 'O(1)'
 *  time for speedy tokenization.
 *  
 *  @author Hunter Bragg
 */
public enum ReservedWord implements IKeyword {
	
	//---------------
	// envision base
	//---------------
	
	/** Used to reference code/data in other envision program files. */
	IMPORT("import"),
	/** Used in conjunction with 'import' statements to denote how to specifically refer to a given import. */
	AS("as"),
	/** Denotes standard function declarations. */
	FUNC("func"),
	/** Denotes class declarations. */
	CLASS("class"),
	/** Denotes class constructor parameters or actions. */
	INIT("init"),
	/** Used within class instance methods to refer to the current specific class instance. */
	THIS("this"),
	/** Used within class methods to refer to a superclass's member. */
	SUPER("super"),
	/** Denotes interface declarations. */
	INTERFACE("interface"),
	/** Used within methods to return some value or exit the current scope. */
	RETURN("return"),
	/** Returns a value iff the condition is true */
	RETIF("retif"),
	/** Hardcoded boolean value -- can be assigned to booleans and generic types */
	TRUE("true"),
	/** Hardcoded boolean value -- can be assigned to booleans and generic types */
	FALSE("false"),
	/** Shortcut to create getter methods for passed variables. */
	GET("get"),
	/** Shortcut to create setter methods for passed variables. */
	SET("set"),
	/** Denotes the start of a new line. */
	NEWLINE("\\NewLine"),
	
	/** Bundles data within the following block into the same pacakge -- similar to 'namespace' in C++/C#. */
	//PACKAGE("package", KeywordType.BASE),
	//READ("read", KeywordType.BASE), //Used to read console input from the user
	//PRINT("print", KeywordType.BASE),
	//PRINTLN("println", KeywordType.BASE),
	
	//---------------
	// data modifier
	//---------------
	
	STATIC("static", KeywordType.DATA_MODIFIER), //Declares a member type to exist outside of a class instance.
	FINAL("final", KeywordType.DATA_MODIFIER), //Prevents a variable value from being changed.
	STRONG("strong", KeywordType.DATA_MODIFIER), //Locks a datatype to the original type it was initialized with (prevents dynamic type changes).
	ABSTRACT("abstract", KeywordType.DATA_MODIFIER), //Defines a class as an abstract base type.
	//OVERRIDE("override", KeywordType.DATA_MODIFIER), //Overrides functionality of a method from an inherited parent.
	
	//---------------
	// operator type
	//---------------
	
	/**
	 *  Denotes the start of an Operator Overload function.
	 */
	OPERATOR_("operator"), //Used to incate operator overloading
	//MODULAR_VALUE("@", KeywordType.OPERATOR_TYPE), //Used for modular function declarations
	//OPERATOR_EXPR("~", true, KeywordType.OPERATOR_TYPE), //Used for modular operator expressions
	
	//------------
	// data types
	//------------
	
	/**
	 *  Shorthand wildcard variable type.
	 *  While not strictly necessary, can be used to take on the type of any value.
	 */
	VAR("var", KeywordType.DATATYPE),
	
	/**
	 *  While not strictly necessary, can be used to denote functions which do
	 *  not return any value.
	 */
	VOID("void", KeywordType.DATATYPE),
	NULL("null", KeywordType.DATATYPE),
	
	BOOLEAN("boolean", KeywordType.DATATYPE),
	NUMBER("number", KeywordType.DATATYPE),
	INT("int", KeywordType.DATATYPE),
	DOUBLE("double", KeywordType.DATATYPE),
	CHAR("char", KeywordType.DATATYPE),
	STRING("string", KeywordType.DATATYPE),
	
	ENUM("enum", KeywordType.DATATYPE),
	LIST("list", KeywordType.DATATYPE),
	
	//-------
	// loops
	//-------
	
	FOR("for"), //For loop declaration -- Types: Standard, range, and Lambda.
	DO("do"), //Do statement which is eventually followed by a While loop -- Do While Loop.
	WHILE("while"), //While loop declaration.
	UNTIL("until"), //Until loop declaration.
	
	//--------------
	// loop control
	//--------------
	
	TO("to"), //Used to define a range for a for loop to iterate across.
	UPTO("upto"),
	BY("by"), //Used to indicate an increment amount for range expressions.
	BREAK("break"), //Used to prematurely exit the iteration of a loop or switch statement.
	BREAKIF("breakif"), //Breaks a loop if the given condition is true
	CONTINUE("continue"), //Used to advance to the next iteration when inside of a loop statement.
	CONTIF("contif"), //Continues a loop if the given condition is true
	
	//------------------
	// logic statements
	//------------------
	
	IF("if"), //If statement declaration.
	ELSE("else"), //Else block of an If statement -- can also be used to form 'else if' blocks.
	SWITCH("switch"), //Tests a value against a series of potential matching cases.
	CASE("case"), //A potential match for data being tested in a switch.
	DEFAULT("default"), //A catch-all case for switch statements when no case matches the tested value.
	IS("is"), //Compares the underlying object types of two objects -- instanceof in Java.
	//ISNOT("isnot", KeywordType.LOGIC), //Compares the underlying object types such that the compared is not the same. !(instanceof)
	
	//------------
	// exceptions
	//------------
	
	TRY("try"), //Attempts to execute code that could potentially fail within an isolated block.
	CATCH("catch"), //The block which handles errors thrown from a try block.
	FINALLY("finally"), //A block which will execute after a try block regardless of failure.
	THROW("throw"), //Used to throw an exception at any point in program execution.
	
	//-------------------
	// internal literals
	//-------------------
	
	NUMBER_LITERAL("\\NumLiteral", KeywordType.LITERAL), //A number in token form representing either an integer or a double.
	STRING_LITERAL("\\StringLiteral", KeywordType.LITERAL), //A string in token form.
	CHAR_LITERAL("\\CharLiteral", KeywordType.LITERAL), //A char in token form.
	IDENTIFIER("\\Literal", KeywordType.LITERAL), //A value that could pertain to a object name.
	EOF("\\EOF", KeywordType.LITERAL), //Denotes the end of the current file
	
	;
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	public final String typeString;
	private final int types;
	
	private ReservedWord(String keywordIn, KeywordType... typeIn) {
		typeString = keywordIn;
		
		int m = 0b00000000;
		for (var t : typeIn) m |= t.byte_val;
		types = m;
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	private static HashMap<String, ReservedWord> keywords = new HashMap<>();
	
	static {
		for (var k : values()) keywords.put(k.typeString, k);
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	public boolean hasType(KeywordType type) {
		return (types & type.byte_val) == type.byte_val;
	}
	
	/**
	 * Returns a keyword from the given input String.
	 * If no keywords match, null is returned instead.
	 */
	public static ReservedWord getKeyword(String in) {
		return keywords.getOrDefault(in, null);
	}
	
	/**
	 * Returns true if the specified string is a datatype keyword.
	 */
	public static boolean isDataType(String in) {
		return EUtil.nullApplyR(getKeyword(in), k -> k.isDataType(), false);
	}
	
	/**
	 * Returns true if the specified string is a datatype keyword.
	 */
	public static boolean isDataType(ReservedWord in) {
		return EUtil.nullApplyR(in, k -> k.isDataType(), false);
	}
	
	/**
	 * Returns the EDataType equivalent if this keyword is a datatype.
	 */
	public static Primitives getDataType(String in) {
		return EUtil.nullApplyR(getKeyword(in), k -> k.getDataType(), Primitives.NULL);
	}
	
	/**
	 * Returns the EDataType equivalent if this keyword is a datatype.
	 */
	public static Primitives getDataType(ReservedWord in) {
		return EUtil.nullApplyR(in, k -> k.getDataType(), Primitives.NULL);
	}
	
}
