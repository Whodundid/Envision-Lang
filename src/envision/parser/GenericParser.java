package envision.parser;

import static envision.tokenizer.KeywordType.ARITHMETIC;
import static envision.tokenizer.KeywordType.ASSIGNMENT;
import static envision.tokenizer.KeywordType.DATA_MODIFIER;
import static envision.tokenizer.KeywordType.VISIBILITY_MODIFIER;
import static envision.tokenizer.Operator.COMMA;
import static envision.tokenizer.Operator.CURLY_L;
import static envision.tokenizer.Operator.CURLY_R;
import static envision.tokenizer.Operator.DEC;
import static envision.tokenizer.Operator.GT;
import static envision.tokenizer.Operator.INC;
import static envision.tokenizer.Operator.LT;
import static envision.tokenizer.Operator.SEMICOLON;
import static envision.tokenizer.ReservedWord.AS;
import static envision.tokenizer.ReservedWord.BREAK;
import static envision.tokenizer.ReservedWord.BREAKIF;
import static envision.tokenizer.ReservedWord.BY;
import static envision.tokenizer.ReservedWord.CATCH;
import static envision.tokenizer.ReservedWord.CLASS;
import static envision.tokenizer.ReservedWord.CONTIF;
import static envision.tokenizer.ReservedWord.CONTINUE;
import static envision.tokenizer.ReservedWord.DO;
import static envision.tokenizer.ReservedWord.ENUM;
import static envision.tokenizer.ReservedWord.FALSE;
import static envision.tokenizer.ReservedWord.FINAL;
import static envision.tokenizer.ReservedWord.FINALLY;
import static envision.tokenizer.ReservedWord.FOR;
import static envision.tokenizer.ReservedWord.IF;
import static envision.tokenizer.ReservedWord.IMPORT;
import static envision.tokenizer.ReservedWord.NEWLINE;
import static envision.tokenizer.ReservedWord.RETIF;
import static envision.tokenizer.ReservedWord.RETURN;
import static envision.tokenizer.ReservedWord.STRONG;
import static envision.tokenizer.ReservedWord.SWITCH;
import static envision.tokenizer.ReservedWord.TO;
import static envision.tokenizer.ReservedWord.TRUE;
import static envision.tokenizer.ReservedWord.TRY;
import static envision.tokenizer.ReservedWord.WHILE;

import envision.lang.natives.Primitives;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.expressions.expression_types.Expr_Literal;
import envision.parser.statements.Statement;
import envision.parser.statements.statementParsers.PS_Class;
import envision.parser.statements.statementParsers.PS_Enum;
import envision.parser.statements.statementParsers.PS_For;
import envision.parser.statements.statementParsers.PS_Function;
import envision.parser.statements.statementParsers.PS_GetSet;
import envision.parser.statements.statementParsers.PS_If;
import envision.parser.statements.statementParsers.PS_Import;
import envision.parser.statements.statementParsers.PS_Interface;
import envision.parser.statements.statementParsers.PS_LoopControl;
import envision.parser.statements.statementParsers.PS_ParseDeclaration;
import envision.parser.statements.statementParsers.PS_Return;
import envision.parser.statements.statementParsers.PS_Switch;
import envision.parser.statements.statementParsers.PS_Try;
import envision.parser.statements.statementParsers.PS_VarDef;
import envision.parser.statements.statementParsers.PS_While;
import envision.parser.statements.statement_types.Stmt_Block;
import envision.parser.statements.statement_types.Stmt_Expression;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.IKeyword;
import envision.tokenizer.KeywordType;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

/**
 * As parsing can be broken into either parsing for statements or expressions,
 * the generic parser is the abstract parent of both.
 * <p>
 * Many of the EnvisionParser's token retrival/comparison/navigation methods
 * are mapped directly into this class for child convenience.
 * 
 * @author Hunter Bragg
 */
public abstract class GenericParser {
	
	public static EnvisionParser parser;

	/** Assigned when creating a modular function and will only not be null while building one. */
	//public static BoxHolder<Token, Token> modularValues = null;
	/** Assigned when a class is currently being parsed. Will only not be null while within a class declaration. */
	//public static Token curClassName = null;
	
	//-----------------------------------------------------------------------------------------------------
	
	/**
	 * Begin the parsing process attempting to find a valid statement.
	 */
	public static Statement parse(EnvisionParser parserIn) {
		parser = parserIn;
		return declaration();
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Declaration Start
	//-----------------------------------------------------------------------------------------------------
	// At the start of each statement parse, the parser will attempt to match a statement declaration
	// token -- These are any tokens which signify the beginning of a language level declaration.
	//-----------------------------------------------------------------------------------------------------
	
	public static Statement declaration() { return declaration(false); }
	public static Statement declaration(boolean inMethod) {
		//consume any new line characters or semicolons first -- these by themselves are to be ignored
		while (match(NEWLINE, SEMICOLON)) return null;
		
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
		//these restions are in place due to the fact that the resulting statement declaration
		//would simply not make sense being declared inside of a method
		if (inMethod) {
			errorIf(checkType(VISIBILITY_MODIFIER), "Statements with visibility modifiers cannot be defined inside of a method!");
			errorIf(check(CLASS), "Classes cannot be declared inside of methods!");
			errorIf(check(ENUM), "Enums cannot be declared inside of methods!");
			//errorIf(check(PACKAGE), "Packages cannot be declared inside of methods!");
			
			if (checkType(DATA_MODIFIER)) {
				errorIf(!check(STRONG, FINAL), "Valid modifiers for statements declared inside of methods are 'strong' or 'final'!");
			}
		}
		
		
		
		//begin by attempting to parse a ParserDeclaration
		ParserDeclaration dec = PS_ParseDeclaration.parseDeclaration();
		
		//determine next path
		switch (dec.getDeclarationType()) {
		
		case EXPR:				return expressionStatement();
		case CLASS_DEF: 		return PS_Class.classDeclaration(dec);
		case ENUM_DEF: 			return PS_Enum.enumDeclaration(dec);
		case FUNC_DEF: 			return PS_Function.functionDeclaration(false, false, dec);
		case INIT_DEF:			return PS_Function.functionDeclaration(true, false, dec);
		case OPERATOR_DEF:  	return PS_Function.functionDeclaration(false, true, dec);
		case GETSET: 			return PS_GetSet.getSet(dec);
		case INTERFACE_DEF: 	return PS_Interface.interfaceDeclaration(dec);
		case VAR_DEF: 			return PS_VarDef.variableDeclaration(dec);
		
		//if the code has reached this point, it means a statement declaration has not been found
		//and so the parser will now attempt to find a standard statement beginning.
		case OTHER:
		default: 				return parseStatement();
		}
	}
	
	/**
	 * Returns a statement of some kind that is not the start of a declaration.
	 * @return Statement
	 */
	public static Statement parseStatement() {
		if (match(CURLY_L))				return new Stmt_Block(getBlock());
		if (match(IMPORT))				return PS_Import.handleImport();
		if (match(TRY))					return PS_Try.tryStatement();
		if (match(FOR))					return PS_For.forStatement();
		if (match(SWITCH))				return PS_Switch.switchStatement();
		if (match(IF))					return PS_If.ifStatement();
		if (match(RETURN))				return PS_Return.returnStatement();
		if (match(RETIF))				return PS_Return.returnStatement(true);
		if (check(CONTINUE, CONTIF))	return PS_LoopControl.handleContinue();
		if (check(BREAK, BREAKIF))		return PS_LoopControl.handleBreak();
		if (check(DO, WHILE))			return PS_While.whileStatement();
		
		//if none of the statement beginnings then try to parse the token into an expression
		return expressionStatement();
	}
	
	/**
	 * Attempts to parse an expression from tokens.
	 * @return Statement
	 */
	public static Statement expressionStatement() {
		Expression e = ExpressionParser.parseExpression();
		
		errorIf(e instanceof Expr_Literal, "Invalid declaration start!");
		//errorIf(!match(SEMICOLON, NEWLINE, EOF), "An expression must be followed by either a ';' or a new line!");
		
		return new Stmt_Expression(e);
	}
	
	//-----------------------------------------------------------------------------------------------------
	
	/**
	 * Collects the statements inside of a { } block.
	 * @return EArrayList<Statement>
	 */
	public static EArrayList<Statement> getBlock() { return getBlock(false); }
	public static EArrayList<Statement> getBlock(boolean inMethod) {
		EArrayList<Statement> statements = new EArrayList();
		
		while (!check(CURLY_R) && !atEnd()) {
			Statement s = declaration(inMethod);
			//remove any new lines
			while (match(NEWLINE));
			statements.addIf(s != null, s);
		}
		
		consume(CURLY_R, "Expected '}' after block!");
		return statements;
	}
	
	/**
	 * Parses through the next several tokens attempting to find tokens for parameters.
	 * @param types
	 * @return Token[]
	 */
	public static Token[] getParameters() { return getParameters(null); }
	public static Token[] getParameters(Token typeIn) {
		EArrayList<Token> params = new EArrayList();
		
		//check type
		if (typeIn != null) {
			Primitives datatype = Primitives.getDataType(typeIn);
			
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
				Token parameter = current();
				errorIf(params.contains(parameter), "Duplicate parameter in type!");
				params.add(parameter);
				advance();
			}
			while (match(COMMA));
		}
		
		consume(GT, "Expected '>' to end parameter types!");
		
		Token[] parr = new Token[params.size()];
		for (int i = 0; i < params.size(); i++) {
			parr[i] = params.get(i);
		}
		
		return parr;
	}
	
	//-----------------------------------------------------------------------------------------------------
	// Parser Convenience Methods
	//-----------------------------------------------------------------------------------------------------
	
	public static Token consume(IKeyword keyword, String errorMessage) {
		return parser.consume(errorMessage, keyword);
	}
	
	public static Token consume(String errorMessage, IKeyword... keywords) {
		return parser.consume(errorMessage, keywords);
	}
	
	public static Token consumeType(KeywordType type, String errorMessage) {
		return parser.consumeType(errorMessage, type);
	}
	
	public static Token consumeType(String errorMessage, KeywordType... types) {
		return parser.consumeType(errorMessage, types);
	}
	
	public static boolean match(IKeyword... keywords) {
		return parser.match(keywords);
	}
	
	public static boolean matchType(KeywordType... types) {
		return parser.matchType(types);
	}
	
	public static boolean check(IKeyword... val) { return parser.check(val); }
	public static boolean checkNext(IKeyword... val) { return parser.checkNext(val); }
	public static boolean checkPrevious(IKeyword... val) { return parser.checkPrevious(val); }
	public static boolean checkType(KeywordType... type) { return parser.checkType(type); }
	public static boolean checkNextType(KeywordType... type) { return parser.checkNextType(type); }
	public static boolean checkPreviousType(KeywordType... type) { return parser.checkPreviousType(type); }
	
	public static boolean atEnd() { return current().isEOF(); }
	public static void advance() { parser.advance(); }
	public static Token getAdvance() { return parser.getAdvance(); }
	
	public static void consumeEmptyLines() { parser.consumeEmptyLines(); }
	
	public static Token current() { return parser.current(); }
	public static Token previous() { return parser.previous(); }
	public static Token next() { return parser.next(); }
	
	public static int getCurrentNum() { return parser.getCurrentIndex(); }
	public static void setCurrentNum(int in) { parser.setCurrentIndex(in); }
	public static void setPrevious() { parser.setPrevious(); }
	public static void setPrevious(int n) { for (int i = 0; i < n; i++) parser.setPrevious(); }

	public static void error(String message) {
		throw parser.error(message);
	}
	
	public static void errorPrevious(String message) {
		setPrevious();
		throw parser.error(message);
	}
	
	public static void errorPrevious(int prevAmount, String message) {
		setPrevious(prevAmount);
		throw parser.error(message);
	}
	
	public static void errorIf(boolean condition, String message) {
		if (condition) throw parser.error(message);
	}
	
	public static void errorPreviousIf(boolean condition, String message) {
		if (condition) {
			setPrevious();
			throw parser.error(message);
		}
	}
	
	public static void errorPreviousIf(boolean condition, int prevAmount, String message) {
		if (condition) {
			setPrevious(prevAmount);
			throw parser.error(message);
		}
	}
	
}
