package envision.tokenizer;

import envision.lang.util.EnvisionDataType;
import eutil.EUtil;
import eutil.datatypes.EArrayList;

/** The set of envision reserved keywords and operators. */
public enum Keyword {
	
	//base
	PACKAGE("package", KeywordType.BASE), //Bundles data within the following block into the same pacakge -- similar to 'namespace' in C++/C#.
	IMPORT("import", KeywordType.BASE), //Used to reference code/data in other envision program files.
	AS("as", KeywordType.BASE),
	READ("read", KeywordType.BASE), //Used to read console input from the user
	PRINT("print", KeywordType.BASE),
	PRINTLN("println", KeywordType.BASE),
	TRUE("true", KeywordType.OBJECT), //Hardcoded boolean value -- can be assigned to booleans and generic types
	FALSE("false", KeywordType.OBJECT), //Hardcoded boolean value -- can be assigned to booleans and generic types
	
	//object
	CLASS("class", KeywordType.OBJECT), //Declares a class that can contain members and methods.
	THIS("this", KeywordType.OBJECT), //Used within class methods to refer back to the current its own instance.
	SUPER("super", KeywordType.OBJECT), //Used within class methods to refer to a superclass's member.
	INTERFACE("interface", KeywordType.OBJECT), //Declares an interface.
	INIT("init", KeywordType.OBJECT), //Used within classes as constructors to initialize data upon instance creation.
	
	//special
	GET("get", KeywordType.OBJECT),
	SET("set", KeywordType.OBJECT),
	
	//data modifier
	STATIC("static", KeywordType.DATA_MODIFIER), //Declares a member type to exist outside of a class instance.
	FINAL("final", KeywordType.DATA_MODIFIER), //Prevents a variable value from being changed.
	STRONG("strong", KeywordType.DATA_MODIFIER), //Locks a datatype to the original type it was initialized with (prevents dynamic type changes).
	ABSTRACT("abstract", KeywordType.DATA_MODIFIER), //Defines a class as an abstract base type.
	OVERRIDE("override", KeywordType.DATA_MODIFIER), //Overrides functionality of a method from an inherited parent.
	
	//visibility modifier
	//PRIVATE("private", KeywordType.VISIBILITY_MODIFIER), //Restricts visibility to the immediate scope.
	PROTECTED("_", KeywordType.VISIBILITY_MODIFIER), //Restricts visibility to the scope of the current class instance along with any inherited children.
	//PUBLIC("public", KeywordType.VISIBILITY_MODIFIER), //No visibility restrictions -- meaning anything at any point in the program's execution can access.
	
	//operator type
	OPERATOR_("operator", KeywordType.OPERATOR_TYPE), //Used to incate operator overloading
	MODULAR_VALUE("@", KeywordType.OPERATOR_TYPE), //Used for modular function declarations
	OPERATOR_EXPR("~", true, KeywordType.OPERATOR_TYPE), //Used for modular operator expressions
	
	//return
	RETURN("return", KeywordType.RETURN_TYPE), //Used within methods to return some value or exit the current scope.
	RETIF("retif", KeywordType.RETURN_TYPE), //Returns a value iff the condition is true
	
	//data types
	BOOLEAN("boolean", KeywordType.DATATYPE), //Strongly specify a variable as a boolean
	INT("int", KeywordType.DATATYPE), //Strongly specify a variable as a an integer (non-floating-point number)
	DOUBLE("double", KeywordType.DATATYPE), //Strongly specify a variable as a double (floating-point number)
	CHAR("char", KeywordType.DATATYPE), //Strongly specify a variable as a single character
	STRING("string", KeywordType.DATATYPE), //Strongly specify a variable as a string
	NUMBER("number", KeywordType.DATATYPE), //Strongly specify a variable that will either be an int or a double
	OBJECT("object", KeywordType.DATATYPE), //Generic variable type -- can be used to represent any type
	//LIST("list", KeywordType.DATATYPE), //Effectively an array list declaration
	VOID("void", KeywordType.DATATYPE), //Empty datatype -- used to denote that nothing is returned from a function
	NULL("null", KeywordType.DATATYPE), //Denotes the absense of an actual object value
	ENUM("enum", KeywordType.DATATYPE), //Enumerated type used to hold compile-time constants
	FUNCTION("func", KeywordType.BASE), //Used to denote the start of a function
	//EXCEPTION("exception", KeywordType.DATATYPE), //Used in catch blocks for try statements
	
	//loops
	FOR("for", KeywordType.LOOP_TYPE), //For loop declaration -- Types: Standard, range, and Lambda.
	DO("do", KeywordType.LOOP_TYPE), //Do statement which is eventually followed by a While loop -- Do While Loop.
	WHILE("while", KeywordType.LOOP_TYPE), //While loop declaration.
	UNTIL("until", KeywordType.LOOP_TYPE), //Until loop declaration.
	
	//loop control
	TO("to", KeywordType.LOOP_CONTROL), //Used to define a range for a for loop to iterate across.
	UPTO("upto", KeywordType.LOOP_CONTROL),
	BY("by", KeywordType.LOOP_CONTROL), //Used to indicate an increment amount for range expressions.
	BREAK("break", KeywordType.LOOP_CONTROL), //Used to prematurely exit the iteration of a loop or switch statement.
	BREAKIF("breakif", KeywordType.LOOP_CONTROL), //Breaks a loop if the given condition is true
	CONTINUE("continue", KeywordType.LOOP_CONTROL), //Used to advance to the next iteration when inside of a loop statement.
	CONTIF("contif", KeywordType.LOOP_CONTROL), //Continues a loop if the given condition is true
	
	//logic statements
	IF("if", KeywordType.LOGIC), //If statement declaration.
	ELSE("else", KeywordType.LOGIC), //Else block of an If statement -- can also be used to form 'else if' blocks.
	SWITCH("switch", KeywordType.LOGIC), //Tests a value against a series of potential matching cases.
	CASE("case", KeywordType.LOGIC), //A potential match for data being tested in a switch.
	DEFAULT("default", KeywordType.LOGIC), //A catch-all case for switch statements when no case matches the tested value.
	IS("is", KeywordType.LOGIC), //Compares the underlying object types of two objects -- instanceof in Java.
	ISNOT("isnot", KeywordType.LOGIC), //Compares the underlying object types such that the compared is not the same. !(instanceof)
	
	//exceptions
	TRY("try", KeywordType.EXCEPTION), //Attempts to execute code that could potentially fail within an isolated block.
	CATCH("catch", KeywordType.EXCEPTION), //The block which handles errors thrown from a try block.
	FINALLY("finally", KeywordType.EXCEPTION), //A block which will execute after a try block regardless of failure.
	THROW("throw", KeywordType.EXCEPTION), //Used to throw an exception at any point in program execution.
	
	//internal literals
	NUMBER_LITERAL("\\NumLiteral", KeywordType.LITERAL), //A number in token form representing either an integer or a double.
	STRING_LITERAL("\\StringLiteral", KeywordType.LITERAL), //A string in token form.
	IDENTIFIER("\\Literal", KeywordType.LITERAL), //A value that could pertain to a object name.
	NEWLINE("\\NewLine", KeywordType.LITERAL), //Denotes the start of a new line
	EOF("\\EOF", KeywordType.LITERAL), //Denotes the end of the current file
	
	//enclosing operator
	SCOPE_LEFT("{", true, KeywordType.ENCLOSER), //Start scope blocks.
	SCOPE_RIGHT("}", true, KeywordType.ENCLOSER), //Ends scope blocks.
	EXPR_LEFT("(", true, KeywordType.ENCLOSER), //Stats expressions or parameter/argument data.
	EXPR_RIGHT(")", true, KeywordType.ENCLOSER), //Ends expressions or parameter/argument data.
	BRACKET_LEFT("[", true, KeywordType.ENCLOSER), //Starts list initializer values.
	BRACKET_RIGHT("]", true, KeywordType.ENCLOSER), //End list initializer values.
	ARRAY_OPERATOR("\\[]", true, KeywordType.OPERATOR), //A special type of operator used to override the array brackets.
	
	//terminator
	SEMICOLON(";", true, KeywordType.TERMINATOR), //Concludes the current statement. (generally unnecessary but still permitted)
	
	//separators
	COMMA(",", true, KeywordType.SEPARATOR),
	COLON(":", true, KeywordType.SEPARATOR),
	PERIOD(".", true, KeywordType.SEPARATOR),
	VARARGS("...", true, KeywordType.SEPARATOR),
	
	//comment
	COMMENT("//", true, KeywordType.COMMENT),
	COMMENT_START("/*", true, KeywordType.COMMENT),
	COMMENT_DESCRIPTOR("/**", true, KeywordType.COMMENT),
	COMMENT_END("*/", true, KeywordType.COMMENT),
	
	//logical operators
	COMPARE("==", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	NOT_EQUALS("!=", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	AND("&&", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	OR("||", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	BITWISE_AND("&", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	BITWISE_OR("|", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	BITWISE_XOR("^", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	//BITWISE_NOT("~", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	TERNARY("?", true, KeywordType.LOGICAL),
	NEGATE("!", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	LESS_THAN("<", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	GREATER_THAN(">", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	LESS_THAN_EQUALS("<=", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	GREATER_THAN_EQUALS(">=", true, KeywordType.LOGICAL, KeywordType.OPERATOR),
	LAMBDA("->", true, KeywordType.LOGICAL),
	
	//arithmetic
	ADD("+", true, KeywordType.ARITHMETIC, KeywordType.OPERATOR, KeywordType.VISIBILITY_MODIFIER),
	SUBTRACT("-", true, KeywordType.ARITHMETIC, KeywordType.OPERATOR, KeywordType.VISIBILITY_MODIFIER),
	MULTIPLY("*", true, KeywordType.ARITHMETIC, KeywordType.OPERATOR),
	DIVIDE("/", true, KeywordType.ARITHMETIC, KeywordType.OPERATOR),
	MODULUS("%", true, KeywordType.ARITHMETIC, KeywordType.OPERATOR),
	SHIFT_LEFT("<<", true, KeywordType.ARITHMETIC, KeywordType.OPERATOR),
	SHIFT_RIGHT(">>", true, KeywordType.ARITHMETIC, KeywordType.OPERATOR),
	SHIFT_RIGHT_ARITHMETIC(">>>", true, KeywordType.ARITHMETIC, KeywordType.OPERATOR),
	
	//assignment
	ASSIGN("=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	ADD_ASSIGN("+=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	SUBTRACT_ASSIGN("-=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	MULTIPLY_ASSIGN("*=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	DIVIDE_ASSIGN("/=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	MODULUS_ASSIGN("%=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	BITWISE_AND_ASSIGN("&=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	BITWISE_OR_ASSIGN("|=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	BITWISE_XOR_ASSIGN("^=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	SHIFT_LEFT_ASSIGN("<<=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	SHIFT_RIGHT_ASSIGN(">>=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	SHIFT_RIGHT_ARITHMETIC_ASSIGN(">>>=", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	INCREMENT("++", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	POST_INCREMENT("_++", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	DECREMENT("--", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	POST_DECREMENT("_--", true, KeywordType.ASSIGNMENT, KeywordType.OPERATOR),
	
	;
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	public final String chars;
	public final KeywordType[] types;
	public final boolean isOperator;
	
	private static EArrayList<Keyword> keywords = new EArrayList();
	private static EArrayList<Keyword> operators = new EArrayList();
	
	private Keyword(String keywordIn, KeywordType... typeIn) { this(keywordIn, false, typeIn); }
	private Keyword(String keywordIn, boolean isOp, KeywordType... typeIn) {
		chars = keywordIn;
		types = typeIn;
		isOperator = isOp;
	}
	
	static {
		for (Keyword k : values()) {
			if (k.isOperator) { operators.add(k); }
			else { keywords.add(k); }
		}
	}
	
	public KeywordType getType() { return types[0]; }
	public KeywordType[] getAllTypes() { return types; }
	
	public boolean hasType(KeywordType type) {
		for (KeywordType t : types) if (t == type) return true;
		return false;
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	/** Returns true if this keyword is a script declaration, member, or parameter. */
	public boolean isScriptBase() { return hasType(KeywordType.BASE); }
	/** Returns true if this keyword is a script declaration, member, or parameter. */
	public boolean isObject() { return hasType(KeywordType.OBJECT); }
	/** Returns true if this keyword is a return statement. */
	public boolean isReturn() { return hasType(KeywordType.RETURN_TYPE); }
	/** Returns true if this keyword is a data modifier. */
	public boolean isDataModifier() { return hasType(KeywordType.DATA_MODIFIER); }
	/** Returns true if this keyword is a visibility modifier. */
	public boolean isVisibilityModifier() { return hasType(KeywordType.VISIBILITY_MODIFIER); }
	/** Returns true if this keyword is a datatype. */
	public boolean isDataType() { return hasType(KeywordType.DATATYPE); }
	/** Returns true if this keyword is loop function. */
	public boolean isLoopType() { return hasType(KeywordType.LOOP_TYPE); }
	/** Returns true if this keyword is loop function. */
	public boolean isLoopControl() { return hasType(KeywordType.LOOP_CONTROL); }
	/** Returns true if this keyword is logical statement. */
	public boolean isLogic() { return hasType(KeywordType.LOGIC); }
	/** Returns true if this keyword is related to exception handling. */
	public boolean isException() { return hasType(KeywordType.EXCEPTION); }
	
	public boolean isEnclosing() { return hasType(KeywordType.ENCLOSER); }
	public boolean isTerminator() { return hasType(KeywordType.TERMINATOR); }
	public boolean isSeparator() { return hasType(KeywordType.SEPARATOR); }
	
	/** Returns true if this keyword is a comment. */
	public boolean isComment() { return hasType(KeywordType.COMMENT); }
	/** Returns true if this keyword is a multi-line comment start. */
	public boolean isCommentStart() { return this.equals(COMMENT) || this.equals(COMMENT_START) || this.equals(COMMENT_DESCRIPTOR); }
	/** Returns true if this keyword is a multi-line comment end. */
	public boolean isCommentEnd() { return this.equals(COMMENT_END); }
	
	/** Returns true if this keyword is a logical operator. */
	public boolean isLogical() { return hasType(KeywordType.LOGICAL); }
	/** Returns true if this keyword is an arithmetic function. */
	public boolean isArithmetic() { return hasType(KeywordType.OPERATOR); }
	/** Returns true if this keyword is an arithmetic operator. */
	public boolean isAssignment() { return hasType(KeywordType.ASSIGNMENT); }
	/** Returns true if this keyword is a literal. */
	public boolean isLiteral() { return hasType(KeywordType.LITERAL); }
	
	/** Returns the associated EDataType type of this keyword. If this keyword is not a datatype, EDataType.NULL is returned instead. */
	public EnvisionDataType getDataType() { return EnvisionDataType.getDataType(this); }
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	/** Returns a keyword from the given input String. If no keywords match, null is returned instead. */
	public static Keyword getKeyword(String in) {
		if (in != null) {
			for (Keyword k : keywords) if (k.chars.equals(in)) return k;
		}
		return null;
	}
	
	/** Returns a keyword from the given input String. If no keywords match, null is returned instead. */
	public static Keyword getOperator(String in) {
		if (in != null) {
			for (Keyword k : operators) if (k.chars.equals(in)) return k;
		}
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	/** Returns true if the given string is a keyword. */
	public static boolean isKeyword(String in) {
		if (in != null) {
			for (Keyword k : keywords) if (k.chars.equals(in)) return true;
		}
		return false;
	}
	
	/** Returns true if the given char is the first letter to the start of a keyword. */
	public static boolean isKeywordStart(char c) {
		for (Keyword k : keywords) if (k.chars.charAt(0) == c) return true;
		return false;
	}
	
	/** Returns true if the current 'soFar' String + next is still part of a keyword. */
	public static boolean isStillKeyword(String soFar, char next) {
		if (soFar != null) {
			for (Keyword k : keywords) {
				String word = k.chars;
				if (word.startsWith(soFar) && (word.length() > soFar.length()) && word.charAt(soFar.length()) == next) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isOperatorStart(char c) {
		for (Keyword o : operators) if (o.chars.charAt(0) == c) return true;
		return false;
	}
	
	//(k : operators) -> (k.chars.startsWith(in)) ? k : null
	
	public static Keyword isStillOperator(String in) {
		for (Keyword k : operators) if (k.chars.startsWith(in)) return k;
		return null;
	}
	
	/** Returns true if the given name is allowed to be a script variable or function name. */
	public static boolean isAllowedName(String nameIn) {
		if (nameIn != null) {
			//don't allow names to start with numbers
			if (Character.isDigit(nameIn.charAt(0))) { return false; }
			
			//StringUtils.containsAny(cs, searchChars)
			String regex = ".*[=!?^&|~<>;:\\[\\]+\\-*/%(){}#@.,$\\\"\\'\\\\].*";
			
			//don't allow special characters in names
			if (nameIn.matches(regex)) { return false; }
			
			//don't allow any names that are the same as a keyword
			for (Keyword k : values()) {
				if (k.chars.equals(nameIn)) { return false; }
			}
			
			return true;
		}
		return false;
	}
	
	public static Keyword makePost(Keyword k) {
		if (k == INCREMENT) return POST_INCREMENT;
		if (k == DECREMENT) return POST_DECREMENT;
		throw new RuntimeException("Not a valid operator for 'post' conversion!");
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	/** Returns true if the specified string is a script base keyword. */
	public static boolean isScriptBase(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isScriptBase(), false); }
	/** Returns true if the specified string is an object keyword. */
	public static boolean isObject(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isObject(), false); }
	/** Returns true if the specified string is a return keyword. */
	public static boolean isReturn(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isReturn(), false); }
	/** Returns true if the specified string is a data modifier keyword. */
	public static boolean isDataModifier(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isDataModifier(), false); }
	/** Returns true if the specified string is a visibility keyword. */
	public static boolean isVisibilityModifier(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isVisibilityModifier(), false); }
	/** Returns true if the specified string is a datatype keyword. */
	public static boolean isDataType(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isDataType(), false); }
	/** Returns true if the specified string is a loop keyword. */
	public static boolean isLoopType(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isLoopType(), false); }
	/** Returns true if the specified string is a loop keyword. */
	public static boolean isLoopControl(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isLoopControl(), false); }
	/** Returns true if the specified string is a logical statement keyword. */
	public static boolean isLogic(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isLogic(), false); }
	/** Returns true if the specified string is an exception statement keyword. */
	public static boolean isException(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.isException(), false); }
	
	/** Returns true if the specified string is an enclosing operator. */
	public static boolean isEnclosing(String in) { return EUtil.nullApplyR(getOperator(in), o -> o.isEnclosing(), false); }
	/** Returns true if the specified string is a terminating operator. */
	public static boolean isTerminator(String in) { return EUtil.nullApplyR(getOperator(in), o -> o.isTerminator(), false); }
	/** Returns true if the specified string is a separating operator. */
	public static boolean isSeparator(String in) { return EUtil.nullApplyR(getOperator(in), o -> o.isSeparator(), false); }
	/** Returns true if the specified string is a comment operator. */
	public static boolean isComment(String in) { return EUtil.nullApplyR(getOperator(in), o -> o.isComment(), false); }
	/** Returns true if the specified string is a logical operator. */
	public static boolean isLogical(String in) { return EUtil.nullApplyR(getOperator(in), o -> o.isLogical(), false); }
	/** Returns true if the specified string is an arithmetic operator. */
	public static boolean isArithmetic(String in) { return EUtil.nullApplyR(getOperator(in), o -> o.isArithmetic(), false); }
	/** Returns true if the specified string is an assignment operator. */
	public static boolean isAssignment(String in) { return EUtil.nullApplyR(getOperator(in), o -> o.isAssignment(), false); }

	//-----------------------------------------------------------------------------------------------------------------------------
	
	/** Returns true if the specified string is a script base keyword. */
	public static boolean isScriptBase(Keyword in) { return EUtil.nullApplyR(in, k -> k.isScriptBase(), false); }
	/** Returns true if the specified string is an object keyword. */
	public static boolean isObject(Keyword in) { return EUtil.nullApplyR(in, k -> k.isObject(), false); }
	/** Returns true if the specified string is a return keyword. */
	public static boolean isReturn(Keyword in) { return EUtil.nullApplyR(in, k -> k.isReturn(), false); }
	/** Returns true if the specified string is a data modifier keyword. */
	public static boolean isDataModifier(Keyword in) { return EUtil.nullApplyR(in, k -> k.isDataModifier(), false); }
	/** Returns true if the specified string is a visibility keyword. */
	public static boolean isVisibilityModifier(Keyword in) { return EUtil.nullApplyR(in, k -> k.isVisibilityModifier(), false); }
	/** Returns true if the specified string is a datatype keyword. */
	public static boolean isDataType(Keyword in) { return EUtil.nullApplyR(in, k -> k.isDataType(), false); }
	/** Returns true if the specified string is a loop keyword. */
	public static boolean isLoopType(Keyword in) { return EUtil.nullApplyR(in, k -> k.isLoopType(), false); }
	/** Returns true if the specified string is a loop keyword. */
	public static boolean isLoopControl(Keyword in) { return EUtil.nullApplyR(in, k -> k.isLoopControl(), false); }
	/** Returns true if the specified string is a logical statement keyword. */
	public static boolean isLogic(Keyword in) { return EUtil.nullApplyR(in, k -> k.isLogic(), false); }
	/** Returns true if the specified string is a loop keyword. */
	public static boolean isLoop(Keyword in) { return EUtil.nullApplyR(in, k -> k.isLoopType(), false); }
	/** Returns true if the specified string is a logical statement keyword. */
	public static boolean isException(Keyword in) { return EUtil.nullApplyR(in, k -> k.isException(), false); }
	
	/** Returns true if the specified operator is an enclosing operator. */
	public static boolean isEnclosing(Keyword in) { return EUtil.nullApplyR(in, o -> o.isEnclosing(), false); }
	/** Returns true if the specified operator is a terminating operator. */
	public static boolean isTerminator(Keyword in) { return EUtil.nullApplyR(in, o -> o.isTerminator(), false); }
	/** Returns true if the specified operator is a separating operator. */
	public static boolean isSeparator(Keyword in) { return EUtil.nullApplyR(in, o -> o.isSeparator(), false); }
	/** Returns true if the specified operator is a comment operator. */
	public static boolean isComment(Keyword in) { return EUtil.nullApplyR(in, o -> o.isComment(), false); }
	/** Returns true if the specified operator is a logical operator. */
	public static boolean isLogical(Keyword in) { return EUtil.nullApplyR(in, o -> o.isLogical(), false); }
	/** Returns true if the specified operator is an arithmetic operator. */
	public static boolean isArithmetic(Keyword in) { return EUtil.nullApplyR(in, o -> o.isArithmetic(), false); }
	/** Returns true if the specified operator is an assignment. */
	public static boolean isAssignment(Keyword in) { return EUtil.nullApplyR(in, o -> o.isAssignment(), false); }
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	/** Returns the EDataType equivalent if this keyword is a datatype. */
	public static EnvisionDataType getDataType(String in) { return EUtil.nullApplyR(getKeyword(in), k -> k.getDataType(), EnvisionDataType.NULL); }
	/** Returns the EDataType equivalent if this keyword is a datatype. */
	public static EnvisionDataType getDataType(Keyword in) { return EUtil.nullApplyR(in, k -> k.getDataType(), EnvisionDataType.NULL); }
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	public static enum KeywordType {
		BASE,
		OBJECT,
		DATA_MODIFIER,
		VISIBILITY_MODIFIER,
		OPERATOR_TYPE,
		RETURN_TYPE,
		DATATYPE,
		LOOP_TYPE,
		LOOP_CONTROL,
		LOGIC,
		EXCEPTION,
		LITERAL,
		
		ENCLOSER,
		TERMINATOR,
		SEPARATOR,
		COMMENT,
		LOGICAL,
		OPERATOR,
		ARITHMETIC,
		ASSIGNMENT,
		;
	}
	
}
