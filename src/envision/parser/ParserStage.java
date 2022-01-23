package envision.parser;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.*;

import envision.parser.expressions.Expression;
import envision.parser.expressions.stages.PE_0_Assignment;
import envision.parser.expressions.stages.PE_1_Or;
import envision.parser.expressions.stages.PE_2_And;
import envision.parser.expressions.stages.PE_3_Equality;
import envision.parser.expressions.stages.PE_4_Lambda;
import envision.parser.expressions.stages.PE_5_Term;
import envision.parser.expressions.stages.PE_6_Factor;
import envision.parser.expressions.stages.PE_7_Unary;
import envision.parser.expressions.stages.PE_8_Range;
import envision.parser.expressions.stages.PE_9_MethodCall;
import envision.parser.expressions.stages.PE_A_Primary;
import envision.parser.expressions.types.LiteralExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.stages.PS_Block;
import envision.parser.statements.stages.PS_Class;
import envision.parser.statements.stages.PS_DataModifiers;
import envision.parser.statements.stages.PS_Enum;
import envision.parser.statements.stages.PS_For;
import envision.parser.statements.stages.PS_Generics;
import envision.parser.statements.stages.PS_GetSet;
import envision.parser.statements.stages.PS_If;
import envision.parser.statements.stages.PS_Import;
import envision.parser.statements.stages.PS_Interface;
import envision.parser.statements.stages.PS_LoopControl;
import envision.parser.statements.stages.PS_Method;
import envision.parser.statements.stages.PS_Package;
import envision.parser.statements.stages.PS_Return;
import envision.parser.statements.stages.PS_Switch;
import envision.parser.statements.stages.PS_Try;
import envision.parser.statements.stages.PS_Type;
import envision.parser.statements.stages.PS_Var;
import envision.parser.statements.stages.PS_Visibility;
import envision.parser.statements.stages.PS_While;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.statementUtil.StatementUtil;
import envision.parser.statements.types.BlockStatement;
import envision.parser.statements.types.ExpressionStatement;
import envision.tokenizer.Keyword;
import envision.tokenizer.Keyword.KeywordType;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxHolder;

public class ParserStage {
	
	public static EnvisionParser parser;

	/** Assigned when creating a modular function and will only not be null while building one. */
	public static BoxHolder<Token, Token> modularValues = null;
	/** Assigned when a class is currently being parsed. Will only not be null while within a class declaration. */
	public static Token curClassName = null;
	
	//-------------------------------------------------------------------------------------------------
	
	/** Begin the parsing process attempting to find a valid statement. */
	public static Statement parse(EnvisionParser parserIn) {
		parser = parserIn;
		return declaration();
	}
	
	//-------------------------------------------------------------------------------------------------
	// Declaration Start
	//-------------------------------------------------------------------------------------------------
	// At the start of each statement parse, the parser will attempt to match a statement declaration
	// token -- These are any tokens which signify the beginning of a language level declaration.
	// I.E. Variables, Methods, Enums, Classes, etc.
	//-------------------------------------------------------------------------------------------------
	
	public static Statement declaration() { return declaration(false, null); }
	public static Statement declaration(ParserDeclaration declaration) { return declaration(false, declaration); }
	public static Statement declaration(boolean inMethod) { return declaration(inMethod, null); }
	public static Statement declaration(boolean inMethod, ParserDeclaration declaration) {
		//consume any new line characters or semicolons first -- these by themselves are to be ignored
		while (match(NEWLINE, SEMICOLON)) { return null; }
		
		//break out if the end of the tokens has been reached
		if (atEnd()) { return null; }
		
		//prevent invalid statement beginnings
		errorIf(check(TRUE, FALSE), "Invalid declaration start!");
		errorIf(check(OPERATOR_), "Invalid declaration start!");
		errorIf(check(AS, TO, BY, CATCH, FINALLY), "Invalid declaration start!");
		errorIf(checkType(ARITHMETIC) && !checkType(KeywordType.VISIBILITY_MODIFIER), "Invalid declaration start!");
		errorIf(checkType(SEPARATOR, LOGICAL, KeywordType.COMMENT), "Invalid declaration start!");
		errorIf(checkType(ASSIGNMENT) && !check(INCREMENT, DECREMENT), "Invalid declaration start!");
		
		//prevent invalid statement declarations inside of methods
		//these restions are in place due to the fact that the resulting statement declaration
		//simply would not make sense being declared inside of a method
		if (inMethod) {
			errorIf(checkType(VISIBILITY_MODIFIER), "Statements with visibility modifiers cannot be defined inside of a method!");
			errorIf(check(CLASS), "Classes cannot be declared inside of methods!");
			errorIf(check(ENUM), "Enums cannot be declared inside of methods!");
			errorIf(check(PACKAGE), "Packages cannot be declared inside of methods!");
			
			if (checkType(DATA_MODIFIER)) {
				errorIf(!check(STRONG, FINAL), "Valid modifiers for statements declared inside of methods are 'strong' or 'final'!");
			}
		}
		
		//standard language declaration statements
		
		if (checkType(VISIBILITY_MODIFIER)) { return handleVisibility(declaration); }
		if (checkType(DATA_MODIFIER)) { return handleDataModifier(declaration); }
		if (check(GET, SET)) { return getSet(declaration); }
		if (match(PACKAGE)) { return packageDeclaration(declaration); }
		if (match(ENUM)) { return enumDeclaration(declaration); }
		if (check(IDENTIFIER/*, THIS*/) || checkType(DATATYPE)) { return handleType(declaration); }
		if (match(CLASS)) { return classDeclaration(declaration); }
		if (match(INTERFACE)) { return handleInterface(declaration); }
		if (match(OPERATOR_)) { return methodDeclaration(new ParserDeclaration()); }

		//if the code has reached this point, it means a statement declaration has not been found
		//and so the parser will now attempt to find a standard statement beginning.
		return statement();
	}
	
	/**
	 * Returns a statement of some kind that is not the start of a declaration.
	 * @return Statement
	 */
	public static Statement statement() {
		if (match(IMPORT)) return handleImport();
		if (match(TRY)) return tryStatement();
		if (check(CONTINUE, CONTIF)) return handleContinue();
		if (check(BREAK, BREAKIF)) return handleBreak();
		if (check(DO, WHILE)) return whileStatement();
		if (match(FOR)) return forStatement();
		if (match(SWITCH)) return switchStatement();
		if (match(IF)) return ifStatement();
		if (match(RETURN)) return returnStatement(false);
		if (match(RETIF)) return returnStatement(true);
		if (match(SCOPE_LEFT)) return new BlockStatement(StatementUtil.getBlock());
		
		//if none of the statement beginnings then try to parse the token into an expression
		return expressionStatement();
	}
	
	/**
	 * Attempts to parse an expression from tokens.
	 * @return Statement
	 */
	public static Statement expressionStatement() {
		Expression e = expression();
		errorIf(e instanceof LiteralExpression, "Invalid declaration start!");
		errorIf(!match(SEMICOLON, NEWLINE, EOF), "An expression must be followed by either a ';' or a new line!");
		//errorIf(e instanceof BinaryExpression)
		return new ExpressionStatement(e);
	}
	
	//-------------------------------------------------------------------------------------------------
	// Statements
	//-------------------------------------------------------------------------------------------------
	
	public static EArrayList<Statement> handleBlock() { return PS_Block.handleBlock(new ParserDeclaration()); }
	public static EArrayList<Statement> handleBlock(ParserDeclaration declaration) { return PS_Block.handleBlock(declaration); }
	public static Statement handleBreak() { return PS_LoopControl.handleBreak(); }
	public static Statement handleContinue() { return PS_LoopControl.handleContinue(); }
	public static Statement classDeclaration() { return PS_Class.classDeclaration(new ParserDeclaration()); }
	public static Statement classDeclaration(ParserDeclaration declaration) { return PS_Class.classDeclaration(declaration); }
	public static Statement handleInterface(ParserDeclaration declaration) { return PS_Interface.handleInterface(declaration); }
	public static Statement handleDataModifier() { return PS_DataModifiers.handleDataModifier(new ParserDeclaration()); }
	public static Statement handleDataModifier(ParserDeclaration declaration) { return PS_DataModifiers.handleDataModifier(declaration); }
	public static Statement getSet() { return PS_GetSet.getSet(); }
	public static Statement getSet(ParserDeclaration declaration) { return PS_GetSet.getSet(declaration); }
	public static Statement enumDeclaration() { return PS_Enum.enumDeclaration(new ParserDeclaration()); }
	public static Statement enumDeclaration(ParserDeclaration declaration) { return PS_Enum.enumDeclaration(declaration); }
	public static Statement exceptionStatement() { return null; }
	public static Statement forStatement() { return PS_For.forStatement(); }
	public static Statement handleGenerics() { return PS_Generics.handleGenerics(new ParserDeclaration()); }
	public static Statement handleGenerics(ParserDeclaration declaration) { return PS_Generics.handleGenerics(declaration); }
	public static Statement ifStatement() { return PS_If.ifStatement(); }
	public static Statement handleImport() { return PS_Import.handleImport(); }
	public static Statement methodDeclaration(ParserDeclaration declaration) { return PS_Method.methodDeclaration(false, declaration); }
	public static Statement methodDeclaration(boolean operator) { return PS_Method.methodDeclaration(operator, new ParserDeclaration()); }
	public static Statement methodDeclaration(boolean operator, ParserDeclaration declaration) { return PS_Method.methodDeclaration(operator, declaration); }
	public static Statement packageDeclaration() { return PS_Package.packageDeclaration(new ParserDeclaration()); }
	public static Statement packageDeclaration(ParserDeclaration declaration) { return PS_Package.packageDeclaration(declaration); }
	public static Statement returnStatement() { return PS_Return.returnStatement(false); }
	public static Statement returnStatement(boolean condition) { return PS_Return.returnStatement(condition); }
	public static Statement switchStatement() { return PS_Switch.switchStatement(); }
	public static Statement tryStatement() { return PS_Try.tryStatement(); }
	public static Statement handleType() { return PS_Type.handleType(new ParserDeclaration()); }
	public static Statement handleType(ParserDeclaration declaration) { return PS_Type.handleType(declaration); }
	public static Statement varDeclaration() { return PS_Var.varDeclaration(null, new ParserDeclaration()); }
	public static Statement varDeclaration(Token name, ParserDeclaration declaration) { return PS_Var.varDeclaration(name, declaration); }
	public static Statement handleVisibility() { return PS_Visibility.handleVisibility(null); }
	public static Statement handleVisibility(ParserDeclaration declaration) { return PS_Visibility.handleVisibility(declaration); }
	public static Statement whileStatement() { return PS_While.whileStatement(); }
	
	//-------------------------------------------------------------------------------------------------
	// Expressions
	//-------------------------------------------------------------------------------------------------
	// Expression parsing utilizes a tree walk interpreter approach in order to isoloate expression
	// statements from tokens.
	//-------------------------------------------------------------------------------------------------
	
	public static Expression expression() { return assignment(); }
	public static Expression assignment() { return PE_0_Assignment.run(); }
	public static Expression or() { return PE_1_Or.run(); }
	public static Expression and() { return PE_2_And.run(); }
	public static Expression equality() { return PE_3_Equality.run(); }
	public static Expression lambda() { return PE_4_Lambda.run(); }
	public static Expression term() { return PE_5_Term.run(); }
	public static Expression factor() { return PE_6_Factor.run(); }
	public static Expression unary() { return PE_7_Unary.run(); }
	public static Expression range() { return PE_8_Range.run(); }
	public static Expression call() { return PE_9_MethodCall.run(); }
	public static Expression primary() { return PE_A_Primary.run(); }
	
	//--------------------------------------------
	
	public static EArrayList<Statement> getBlock() { return StatementUtil.getBlock(false); }
	public static EArrayList<Statement> getBlock(boolean inMethod) { return StatementUtil.getBlock(inMethod); }
	public static Token[] getParameters() { return StatementUtil.getParameters(null); }
	public static Token[] getParameters(Token typeIn) { return StatementUtil.getParameters(typeIn); }
	
	//--------------------------------------------
	
	public static Token consume(Keyword k, String m) { return consume(k, meth(), m); }
	public static Token consumeType(Keyword.KeywordType t, String m) { return consume(t, meth(), m); }
	public static boolean match(Keyword... keys) { return match(meth(), keys); }
	public static boolean matchType(KeywordType... keys) { return matchType(meth(), keys); }
	
	private static String meth() {
		StackTraceElement[] e = new Exception().getStackTrace();
		return e[2].getMethodName() + ":" + e[2].getLineNumber();
	}
	
	//--------------------------------------------
	// Parser Convenience Methods
	//--------------------------------------------
	
	public static Token consumeAny(String requirementMessage, Keyword... types) { return parser.consumeAny(requirementMessage, types); }
	public static Token consumeAnyType(String requirementMessage, Keyword.KeywordType... types) { return parser.consumeAnyType(requirementMessage, types); }
	
	public static Token consume(String requirementMessage, Keyword... type) { return parser.consume(requirementMessage, type); }
	public static Token consumeType(String requirementMessage, Keyword.KeywordType... type) { return parser.consumeType(requirementMessage, type);	}
	
	public static Token consume(Keyword type, String methodName, String m) { return parser.consume(type, methodName, m); }
	public static Token consume(Keyword.KeywordType type, String methodName, String m) { return parser.consumeType(type, methodName, m); }
	public static Token consumeType(Keyword.KeywordType type, String methodName, String m) { return parser.consumeType(type, methodName, m); }
	
	public static boolean match(String methodName, Keyword... val) { return parser.match(methodName, val); }
	public static boolean matchType(String methodName, Keyword.KeywordType... type) { return parser.matchType(methodName, type); }
	
	public static boolean matchBoth(Keyword first, Keyword second) { return parser.matchBoth(first, second); }
	public static boolean checkAll(Keyword... in) { return parser.checkAll(in); }
	
	public static boolean check(Keyword... val) { return parser.check(val); }
	public static boolean checkNext(Keyword... val) { return parser.checkNext(val); }
	public static boolean checkPrevious(Keyword... val) { return parser.checkPrevious(val); }
	public static boolean checkType(Keyword.KeywordType... type) { return parser.checkType(type); }
	public static boolean checkNextType(Keyword.KeywordType... type) { return parser.checkNextType(type); }
	public static boolean checkPreviousType(Keyword.KeywordType... type) { return parser.checkPreviousType(type); }
	
	public static boolean checkAdvance(Keyword k) { return parser.checkAdvance(k); }
	public static boolean checkAdvance(Keyword.KeywordType k) { return parser.checkAdvance(k); }
	
	public static boolean atEnd() { return current().isEOF(); }
	public static Token advance() { return parser.advance(); }
	public static Token getAdvance() { return parser.getAdvance(); }
	
	public static Token current() { return parser.current(); }
	public static Token previous() { return parser.previous(); }
	public static Token next() { return parser.next(); }
	
	public static int getCurrentNum() { return parser.getCurrentNum(); }
	public static void setCurrentNum(int in) { parser.setCurrentNum(in); }
	public static void setPrevious() { parser.setPrevious(); }
	public static void setPrevious(int n) { for (int i = 0; i < n; i++) { parser.setPrevious(); } }

	public static void errorIf(boolean condition, String message) { if (condition) error(message); }
	public static void error(String message) { throw parser.error(message); }
	
}
