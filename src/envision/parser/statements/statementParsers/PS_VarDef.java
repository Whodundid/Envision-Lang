package envision.parser.statements.statementParsers;

import static envision.tokenizer.KeywordType.*;
import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.lang.util.Primitives;
import envision.lang.util.data.DataModifier;
import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_Expression;
import envision.parser.statements.statement_types.Stmt_GetSet;
import envision.parser.statements.statement_types.Stmt_VarDef;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.Token;

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
		Stmt_VarDef varDecStatement = new Stmt_VarDef(declaration, getset);
		
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
