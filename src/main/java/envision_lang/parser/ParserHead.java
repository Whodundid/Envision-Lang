package envision_lang.parser;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.EnvisionLang;
import envision_lang.lang.natives.Primitives;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Literal;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statementParsers.PS_Class;
import envision_lang.parser.statements.statementParsers.PS_For;
import envision_lang.parser.statements.statementParsers.PS_Function;
import envision_lang.parser.statements.statementParsers.PS_If;
import envision_lang.parser.statements.statementParsers.PS_Import;
import envision_lang.parser.statements.statementParsers.PS_LoopControl;
import envision_lang.parser.statements.statementParsers.PS_ParseDeclaration;
import envision_lang.parser.statements.statementParsers.PS_Return;
import envision_lang.parser.statements.statementParsers.PS_Switch;
import envision_lang.parser.statements.statementParsers.PS_Try;
import envision_lang.parser.statements.statementParsers.PS_VarDef;
import envision_lang.parser.statements.statementParsers.PS_While;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.KeywordType;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

/**
 * As parsing can be broken into either parsing for statements or expressions,
 * the generic parser is the abstract parent of both.
 * <p>
 * Many of the EnvisionParser's token retrieval/comparison/navigation methods
 * are mapped directly into this class for child convenience.
 * 
 * @author Hunter Bragg
 */
public abstract class ParserHead {
	
	public static EnvisionLangParser parser;
	
	/** Assigned when creating a modular function and will only not be null while building one. */
	//public static BoxHolder<Token, Token> modularValues = null;
	/** Assigned when a class is currently being parsed. Will only not be null while within a class declaration. */
	//public static Token curClassName = null;
    
    private static volatile int methodDepth = 0;
    private static volatile int classDepth = 0;
    
    public static boolean inMethod() { return methodDepth > 0; }
    public static boolean inClass() { return classDepth > 0; }
    
    public static void pushMethodDef() { methodDepth++; }
    public static void pushClassDef() { classDepth++; }
    
    public static void popMethodDef() { methodDepth--; }
    public static void popClassDef() { classDepth--; }
    
    public static int methodDepth() { return methodDepth; }
    public static int classDepth() { return classDepth; }
	
	//-----------------------------------------------------------------------------------------------------
	
	/**
	 * Begin the parsing process attempting to find a valid statement.
	 */
	public static ParsedStatement parse(EnvisionLangParser parserIn) {
		parser = parserIn;
		
		var declaration = declaration();
		
		return declaration;
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Declaration Start
	//-----------------------------------------------------------------------------------------------------
	// At the start of each statement parse, the parser will attempt to match a statement declaration
	// token -- These are any tokens which signify the beginning of a language level declaration.
	//-----------------------------------------------------------------------------------------------------
	
	public static ParsedStatement declaration() {
		// consume any new line characters or semicolons first -- these by themselves are to be ignored
		ignoreTerminators();
		
		if (EnvisionLang.debugMode) {
			System.out.println("HEAD: " + current() + " : " + current().getLineNum() + " : " + getCurrentParsingIndex());			
		}
		
		//break out if the end of the tokens has been reached
		if (atEnd()) return null;
		
		//prevent invalid statement beginnings
		errorIf(check(TRUE, FALSE), "Invalid declaration start!");
		//errorIf(check(OPERATOR_), "Invalid declaration start!");
		errorIf(check(AS, TO, BY, CATCH, FINALLY), "Invalid declaration start!");
		errorIf(checkType(ARITHMETIC) && !checkType(VISIBILITY_MODIFIER), "Invalid declaration start!");
		//errorIf(checkType(SEPARATOR), "Invalid declaration start!");
		errorIf(checkType(ASSIGNMENT) && !check(INC, DEC), "Invalid declaration start!");
		
		
		
		//prevent invalid statement declarations inside of methods
		//these restrictions are in place due to the fact that the resulting statement declaration
		//would simply not make sense being declared inside of a method
		if (inMethod()) {
			errorIf(checkType(VISIBILITY_MODIFIER), "Statements with visibility modifiers cannot be defined inside of a method!");
			//errorIf(check(CLASS), "Classes cannot be declared inside of methods!");
			//errorIf(check(ENUM), "Enums cannot be declared inside of methods!");
			//errorIf(check(PACKAGE), "Packages cannot be declared inside of methods!");
			
			if (checkType(DATA_MODIFIER)) {
				errorIf(!check(STRONG, FINAL), "Valid modifiers for statements declared inside of methods are 'strong' or 'final'!");
			}
		}
		
		
		
		//begin by attempting to parse a ParserDeclaration
		ParserDeclaration dec = PS_ParseDeclaration.parseDeclaration();
		ParsedStatement parsedStatement = null;
		
		//determine next path
		switch (dec.getDeclarationType()) {
		
		case EXPR:				parsedStatement = expressionStatement(dec);                             break;
		case CLASS_DEF: 		parsedStatement = PS_Class.classDeclaration(dec); 						break;
//		case ENUM_DEF: 			parsedStatement = PS_Enum.enumDeclaration(dec); 						break;
		case FUNC_DEF: 			parsedStatement = PS_Function.functionDeclaration(false, false, dec); 	break;
		case INIT_DEF:			parsedStatement = PS_Function.functionDeclaration(true, false, dec); 	break;
		case OPERATOR_DEF:  	parsedStatement = PS_Function.functionDeclaration(false, true, dec); 	break;
//		case GETSET: 			parsedStatement = PS_GetSet.getSet(dec); 								break;
//		case INTERFACE_DEF: 	parsedStatement = PS_Interface.interfaceDeclaration(dec); 				break;
		case VAR_DEF: 			parsedStatement = PS_VarDef.variableDeclaration(dec); 					break;
		
		//if the code has reached this point, it means a statement declaration has not been found
		//and so the parser will now attempt to find a standard statement beginning.
		case OTHER:
		default: 				parsedStatement = parseStatement(dec);
		}
		
		//consume("Expected either a ';' or a new line to complete statement!", EOF, NEWLINE, SEMICOLON);
		
		// pretty sure this is wrong
		if (dec != null) parsedStatement.setBlockStatement(dec.isBlockingStatement());
		
		return parsedStatement;
	}
	
	public static ParsedStatement parseStatement() {
		return parseStatement(null);
	}
	
	/**
	 * Returns a statement of some kind that is not the start of a declaration.
	 * @return Statement
	 */
	public static ParsedStatement parseStatement(ParserDeclaration dec) {
		//consume any new line characters or semicolons first -- these by themselves are to be ignored
		ignoreTerminators();
		
		if (dec == null) {
			dec = PS_ParseDeclaration.parseDeclaration();
		}
		
		if (match(CURLY_L))				return new Stmt_Block(dec, previous(), getBlock());
		if (check(IMPORT))				return PS_Import.handleImport(dec);
		if (check(TRY))					return PS_Try.tryStatement(dec);
		if (check(FOR))					return PS_For.forStatement(dec);
		if (check(SWITCH))				return PS_Switch.switchStatement(dec);
		if (check(IF))					return PS_If.ifStatement(dec);
		if (check(RETURN))				return PS_Return.returnStatement(dec);
		if (check(RETIF))				return PS_Return.returnStatement(dec, true);
		if (check(CONTINUE, CONTIF))	return PS_LoopControl.handleContinue(dec);
		if (check(BREAK, BREAKIF))		return PS_LoopControl.handleBreak(dec);
		if (check(DO, WHILE))			return PS_While.whileStatement(dec);
		
		//if none of the statement beginnings then try to parse the token into an expression
		return expressionStatement(dec);
	}
	
	/**
	 * Attempts to parse an expression from tokens.
	 * @return Statement
	 */
	public static ParsedStatement expressionStatement(ParserDeclaration dec) {
		ParsedExpression e = ExpressionParser.parseExpression();
		
		errorIf(e instanceof Expr_Literal, "Invalid declaration start!");
		consumeTerminator("An expression statement must be followed by either a ';' or a new line!");
		
		var expr = new Stmt_Expression(e);
		expr.setBlockStatement(dec.isBlockingStatement());
		
		return expr;
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	/**
	 * Collects the statements inside of a { } block.
	 * @return EList<ParsedStatement>
	 */
	public static EList<ParsedStatement> getBlock() {
		EList<ParsedStatement> statements = EList.newList();
		
		ignoreTerminators();
		
		while (!check(CURLY_R) && !atEnd()) {
			ParsedStatement s = declaration();
			if (s != null) statements.add(s);
		}
		
		consume(CURLY_R, "Expected '}' after block!");
		return statements;
	}
	
	/**
	 * Parses through the next several tokens attempting to find tokens for parameters.
	 * @param types
	 * @return Token[]
	 */
	public static Token<?>[] getParameters() { return getParameters(null); }
	public static Token<?>[] getParameters(Token typeIn) {
		EList<Token<?>> params = EList.newList();
		
		//check type
		if (typeIn != null) {
			Primitives datatype = Primitives.getPrimitiveType(typeIn);
			
			if (datatype != null) {
				//check if base type can even have parameters
				if (!Primitives.canBeParameterized(datatype)) {
					error("This datatype cannot be natively parameterized!");
				}
			}
		}
		
		consume(LT, "Expected '<' to start parameter types!");
		
		if (!check(GT)) {
			//grab all parameters
			do {
				Token<?> parameter = current();
				errorIf(params.contains(parameter), "Duplicate parameter in type!");
				params.add(parameter);
				advance();
			}
			while (match(COMMA));
		}
		
		consume(GT, "Expected '>' to end parameter types!");
		
		Token<?>[] parr = new Token<?>[params.size()];
		for (int i = 0; i < params.size(); i++) {
			parr[i] = params.get(i);
		}
		
		return parr;
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Parser Convenience Methods
	//-----------------------------------------------------------------------------------------------------
	
	//===========
	// Consumers
	//===========
	
	public static Token<?> consume(IKeyword keyword, String errorMessage) {
		return parser.consume(errorMessage, keyword);
	}
	
	public static Token<?> consume(String errorMessage, IKeyword keyword) { return parser.consume(errorMessage, keyword); }
	public static Token<?> consume(String errorMessage, IKeyword keywordA, IKeyword keywordB) { return parser.consume(errorMessage, keywordA, keywordB); }
	public static Token<?> consume(String errorMessage, IKeyword... keywords) { return parser.consume(errorMessage, keywords); }
	
	public static Token<?> consumeType(KeywordType type, String errorMessage) {
		return parser.consumeType(errorMessage, type);
	}
	
	public static Token<?> consumeType(String errorMessage, KeywordType type) { return parser.consumeType(errorMessage, type); }
	public static Token<?> consumeType(String errorMessage, KeywordType typeA, KeywordType typeB) { return parser.consumeType(errorMessage, typeA, typeB); }
	public static Token<?> consumeType(String errorMessage, KeywordType... types) { return parser.consumeType(errorMessage, types); }
	
	public static Token<?> consumeTerminator() { return consumeTerminator("Expected either a ';' or a new line to complete statement!"); }
	public static Token<?> consumeTerminator(String errorMessage) {
		return parser.consume(errorMessage, SEMICOLON, NEWLINE, EOF);
	}
	
	public static Token<?> consumeTerminatorEOF() { return consumeTerminatorEOF("Expected either a ';' or a new line to complete statement!"); }
	public static Token<?> consumeTerminatorEOF(String errorMessage) {
		return parser.consume(errorMessage, SEMICOLON, NEWLINE, EOF);
	}
	
	//==========
	// Matchers
	//==========
	
	public static boolean match(IKeyword keyword) { return parser.match(keyword); }
	public static boolean match(IKeyword keywordA, IKeyword keywordB) { return parser.match(keywordA, keywordB); }
	public static boolean match(IKeyword... keywords) { return parser.match(keywords); }
	public static boolean matchType(KeywordType type) { return parser.matchType(type); }
	public static boolean matchType(KeywordType typeA, KeywordType typeB) { return parser.matchType(typeA, typeB); }
	public static boolean matchType(KeywordType... types) { return parser.matchType(types); }
	
	//==========
	// Checkers
	//==========
	
	public static boolean check(IKeyword keyword) { return parser.check(keyword); }
	public static boolean check(IKeyword keywordA, IKeyword keywordB) { return parser.check(keywordA, keywordB); }
	public static boolean check(IKeyword... keywords) { return parser.check(keywords); }
	public static boolean checkNext(IKeyword keyword) { return parser.checkNext(keyword); }
	public static boolean checkNext(IKeyword keywordA, IKeyword keywordB) { return parser.checkNext(keywordA, keywordB); }
	public static boolean checkNext(IKeyword... keywords) { return parser.checkNext(keywords); }
	public static boolean checkPrevious(IKeyword keyword) { return parser.checkPrevious(keyword); }
	public static boolean checkPrevious(IKeyword keywordA, IKeyword keywordB) { return parser.checkPrevious(keywordA, keywordB); }
	public static boolean checkPrevious(IKeyword... keywords) { return parser.checkPrevious(keywords); }
	public static boolean checkType(KeywordType type) { return parser.checkType(type); }
	public static boolean checkType(KeywordType typeA, KeywordType typeB) { return parser.checkType(typeA, typeB); }
	public static boolean checkType(KeywordType... types) { return parser.checkType(types); }
	public static boolean checkNextType(KeywordType type) { return parser.checkNextType(type); }
	public static boolean checkNextType(KeywordType typeA, KeywordType typeB) { return parser.checkNextType(typeA, typeB); }
	public static boolean checkNextType(KeywordType... types) { return parser.checkNextType(types); }
	public static boolean checkPreviousType(KeywordType type) { return parser.checkPreviousType(type); }
	public static boolean checkPreviousType(KeywordType typeA, KeywordType typeB) { return parser.checkPreviousType(typeA, typeB); }
	public static boolean checkPreviousType(KeywordType... types) { return parser.checkPreviousType(types); }
	
	/** Returns true if the next non-terminator token has any of the given keyword types. */
	public static boolean checkNextNonTerminator(IKeyword keyword) { return parser.checkNextNonTerminator(keyword); }
	public static boolean checkNextNonTerminator(IKeyword keywordA, IKeyword keywordB) { return parser.checkNextNonTerminator(keywordA, keywordB); }
	public static boolean checkNextNonTerminator(IKeyword... keywords) { return parser.checkNextNonTerminator(keywords); }
	
	/** Returns true if the previous non-terminator token has any of the given keyword types. */
	public static boolean checkPreviousNonTerminator(IKeyword keyword) { return parser.checkPreviousNonTerminator(keyword); }
	public static boolean checkPreviousTerminator(IKeyword keywordA, IKeyword keywordB) { return parser.checkPreviousNonTerminator(keywordA, keywordB); }
	public static boolean checkPreviousNonTerminator(IKeyword... keywords) { return parser.checkPreviousNonTerminator(keywords); }
	
	public static boolean checkAtIndex(int index, IKeyword keyword) { return parser.checkAtIndex(index, keyword); }
	public static boolean checkAtIndex(int index, IKeyword keywordA, IKeyword keywordB) { return parser.checkAtIndex(index, keywordA, keywordB); }
	public static boolean checkAtIndex(int index, IKeyword... keywords) { return parser.checkAtIndex(index, keywords); }
	
	/**
	 * @return The index of the next token that is not a 'NEWLINE'.
	 */
	private static int findNextNonNL() {
		int start = getCurrentParsingIndex();
		
		incrementParsingIndex();
		int i = getCurrentParsingIndex();
		
		Token<?> curToken = current();
		while (!atEnd() && curToken.getKeyword() == NEWLINE) {
			incrementParsingIndex();
			curToken = current();
			i = getCurrentParsingIndex();
		}
		
		setCurrentParsingIndex(start);
		return i;
	}
	
	/**
	 * @return The index of the previous token that is not a 'NEWLINE'.
	 */
	private static int findPreviousNonNL() {
		int start = getCurrentParsingIndex();
		
		decrementParsingIndex();
		int i = getCurrentParsingIndex();
		
		Token<?> curToken = current();
		while (i > 0 && curToken.getKeyword() == NEWLINE) {
			decrementParsingIndex();
			curToken = current();
			i = getCurrentParsingIndex();
		}
		
		setCurrentParsingIndex(start);
		return i;
	}
	
	public static boolean checkNextNL(IKeyword keyword) { return checkAtIndex(findNextNonNL(), keyword); }
	public static boolean checkNextNL(IKeyword keywordA, IKeyword keywordB) { return checkAtIndex(findNextNonNL(), keywordA, keywordB); }
	public static boolean checkNextNL(IKeyword... keywords) { return checkAtIndex(findNextNonNL(), keywords); }
	
	public static boolean atEnd() { return current().isEOF(); }
	public static void advance() { parser.advance(); }
	public static Token<?> getAdvance() { return parser.getAdvance(); }
	
	public static void ignoreTerminators() { while (!atEnd() && matchType(TERMINATOR)); }
	/** Consumes all new lines. */
	public static void ignoreNL() { while (!atEnd() && match(NEWLINE)); }
	
	public static Token<?> current() { return parser.current(); }
	public static IKeyword currentKeyword() { return parser.currentKeyword(); }
	public static Token<?> previous() { return parser.previous(); }
	public static IKeyword previousKeyword() { return parser.previousKeyword(); }
	public static Token<?> previousNonTerminator() { return parser.previousNonTerminator(); }
	public static Token<?> next() { return parser.next(); }
	public static IKeyword nextKeyword() { return parser.nextKeyword(); }
	public static Token<?> nextNonTerminator() { return parser.nextNonTerminator(); }
	
	public static int getCurrentParsingIndex() { return parser.getCurrentParsingIndex(); }
	public static void setCurrentParsingIndex(int in) { parser.setCurrentParsingIndex(in); }
	public static void decrementParsingIndex() { parser.decrementParsingIndex(); }
	public static void decrementParsingIndex(int n) { for (int i = 0; i < n; i++) parser.decrementParsingIndex(); }
	public static void incrementParsingIndex() { parser.incrementParsingIndex(); }
	public static void incrementParsingIndex(int n) { for (int i = 0; i < n; i++) parser.incrementParsingIndex(); }
	
	/**
	 * Sets the current parsing index to the previous index whose token is a
	 * non 'NEWLILNE' token.
	 */
	public static void setPreviousNonNL() { setCurrentParsingIndex(findPreviousNonNL()); }
	
	/**
	 * Sets the current parsing index to the next index whose token is a non
	 * 'NEWLILNE' token.
	 */
	public static void setNextNonNL() { setCurrentParsingIndex(findNextNonNL()); }
	
	public static void error(String message) {
		throw parser.error(message);
	}
	
	public static void errorPrevious(String message) {
		decrementParsingIndex();
		throw parser.error(message);
	}
	
	public static void errorPrevious(int prevAmount, String message) {
		decrementParsingIndex(prevAmount);
		throw parser.error(message);
	}
	
	public static void errorIf(boolean condition, String message) {
		if (condition) throw parser.error(message);
	}
	
	public static void errorPreviousIf(boolean condition, String message) {
		if (condition) {
			decrementParsingIndex();
			throw parser.error(message);
		}
	}
	
	public static void errorPreviousIf(boolean condition, int prevAmount, String message) {
		if (condition) {
			decrementParsingIndex(prevAmount);
			throw parser.error(message);
		}
	}
	
	/**
	 * Returns a shallow copy of the actual tokens in the parser.
	 */
	public static EList<Token<?>> getTokens() {
		return EList.newList(parser.getTokens());
	}
	
	public static void requireTerminator() {
		consumeType(TERMINATOR, "Expected either a ';' or a new line here to conclude function call!");
	}
	
	public static void requireTerminator(String msg) {
		consumeType(TERMINATOR, msg);
	}
	
	public static boolean areBlockStatementsEnabled() {
        return parser.areBlockStatementsEnabled();
    }
}
