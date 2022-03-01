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
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.statements.ExpressionStatement;
import envision.parser.statements.statements.GetSetStatement;
import envision.parser.statements.statements.VariableStatement;
import envision.tokenizer.Token;

public class PS_VarDec extends GenericParser {
	
	public static Statement variableDeclaration() {
		return variableDeclaration(PS_ParseDeclaration.parseScopeVar());
	}
	
	/**
	 * Attempts to parse a variable(s) from tokens.
	 * @return Statement
	 */
	public static Statement variableDeclaration(ParserDeclaration declaration) {
		errorIf(declaration.hasGenerics(), "Variables can not declare generic types in their declaration!");
		
		Token name = null;
		Token type = declaration.getReturnType();
		
		Primitives checkType = (type != null) ? type.getPrimitiveDataType() : null;
		
		//check for valid variable datatypes
		errorPreviousIf(checkType == Primitives.VOID, 2, "Variable types cannot be void!");
		errorPreviousIf(checkType == Primitives.NULL, 2, "Variable types cannot be null!");
		
		//check for invalid variable data modifiers
		if (!DataModifier.isValid_varDec(declaration)) {
			error("Invalid variable data modifiers for '" + type.lexeme + "'! " + declaration.getMods());
		}
		
		/*
		if (declaration.isStrong()) {
			if (name != null) {
				if (type.compareLexeme(name)) errorPrevious("Invalid duplicate identifier!");
				else errorPrevious(3, "Strong can only not be applied to dynamic variable types!");
			}
		}
		*/
		
		//collect any get/set modifiers
		GetSetStatement getset = null;
		if (checkType(VISIBILITY_MODIFIER) || check(GET, SET)) getset = PS_GetSet.parseGetSetVis();
		
		//define the actual variable statement
		Statement varDecStatement = null;
		
		//check for type-less variable creation
		if (check(ASSIGN)) {
			setPrevious();
			Expression typeless_varDec = ExpressionParser.parseExpression();
			varDecStatement = new ExpressionStatement(typeless_varDec);
		}
		else {
			varDecStatement = new VariableStatement(declaration, getset);
			
			//parse for declared variables
			do {
				name = (name == null) ? consume(IDENTIFIER, "Expected a variable name!") : name;
				
				//parse for initializer (if there is one)
				Expression initializer = null;
				if (match(ASSIGN)) initializer = ExpressionParser.parseExpression();
				
				//add variable to statement
				((VariableStatement) varDecStatement).addVar(name, initializer);
				name = null;
			}
			while (match(COMMA));
		}
		
		errorIf(!match(SEMICOLON, NEWLINE, EOF), "Incomplete variable declaration!");
		return varDecStatement;
	}
	
}
