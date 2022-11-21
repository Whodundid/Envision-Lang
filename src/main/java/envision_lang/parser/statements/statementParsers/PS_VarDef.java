package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.KeywordType.*;
import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.lang.natives.Primitives;
import envision_lang.lang.util.DataModifier;
import envision_lang.parser.GenericParser;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.parser.statements.statement_types.Stmt_GetSet;
import envision_lang.parser.statements.statement_types.Stmt_VarDef;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;

public class PS_VarDef extends GenericParser {
	
	public static Statement variableDeclaration() {
		return variableDeclaration(PS_ParseDeclaration.parseScopeVar());
	}
	
	/**
	 * Attempts to parse a variable(s) from tokens.
	 * @return Statement
	 */
	public static Statement variableDeclaration(ParserDeclaration declaration) {
		errorIf(declaration.hasGenerics(), "Variables can not declare generic types in their declaration!");
		
		Token type = null;
		Token name = null;
		
		//try to consume a datatype
		if (checkType(DATATYPE)) type = consumeType(DATATYPE, "Expected a valid datatype!");
		else type = getAdvance();
		
		//check for list-set-expression
		if (check(BRACKET_L)) {
			setPrevious();
			Expression listIndexSet = ExpressionParser.parseExpression();
			return new Stmt_Expression(listIndexSet);
		}
		
		Primitives checkType = (type != null) ? type.getPrimitiveDataType() : Primitives.VAR;
		
		//check for valid variable datatypes
		errorPreviousIf(checkType == Primitives.VOID, 2, "Variable types cannot be void!");
		errorPreviousIf(checkType == Primitives.NULL, 2, "Variable types cannot be null!");
		
		//check for invalid variable data modifiers
		if (!DataModifier.isValid_varDec(declaration)) {
			error("Invalid variable data modifiers for '" + type.lexeme + "'! " + declaration.getMods());
		}
		
		//collect any get/set modifiers
		Stmt_GetSet getset = null;
		if (checkType(VISIBILITY_MODIFIER) || check(GET, SET)) getset = PS_GetSet.parseGetSetVis();
		
		//check for type-less variable creation
		if (check(ASSIGN)) {
			setPrevious();
			Expression typeless_varDec = ExpressionParser.parseExpression();
			return new Stmt_Expression(typeless_varDec);
		}
		
		//define the actual variable statement
		Stmt_VarDef varDecStatement = new Stmt_VarDef(declaration.getStartToken(), declaration, getset);
		
		//parse for declared variables
		do {
			name = (name == null) ? consume(IDENTIFIER, "Expected a variable name!") : name;
			
			//parse for initializer (if there is one)
			Expression initializer = null;
			if (match(ASSIGN)) initializer = ExpressionParser.parseExpression();
			
			//add variable to statement
			varDecStatement.addVar(name, initializer);
			name = null;
		}
		while (match(COMMA));
		
		errorIf(!match(SEMICOLON, NEWLINE, EOF), "Incomplete variable declaration!");
		return varDecStatement;
	}
	
}
