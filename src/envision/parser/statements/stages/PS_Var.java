package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.*;

import envision.lang.util.EnvisionDataType;
import envision.lang.util.data.DataModifier;
import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.VariableStatement;
import envision.tokenizer.Token;

public class PS_Var extends ParserStage {
	
	/**
	 * Attempts to parse a variable(s) from tokens.
	 * @return Statement
	 */
	public static Statement varDeclaration() { return varDeclaration(null, new ParserDeclaration()); }
	public static Statement varDeclaration(Token name, ParserDeclaration declaration) {
		errorIf(declaration.hasGenerics(), "Variables can not declare generic types in their declaration!");
		
		Token type = declaration.getReturnType();
		//check for valid variable datatypes
		if (type.getDataType() == EnvisionDataType.VOID) { setPrevious(2); error("Variable types cannot be void!"); }
		if (type.getDataType() == EnvisionDataType.NULL) { setPrevious(2); error("Variable types cannot be null!"); }
		
		//check for invalid variable data modifiers
		if (!DataModifier.checkVariable(declaration.getMods())) {
			error("Invalid variable data modifiers for '" + type.lexeme + "'! " + declaration.getMods());
		}
		
		if (declaration.getMods().contains(DataModifier.STRONG)) {
			if (name != null) {
				if (type.lexeme.equals(name.lexeme)) {
					setPrevious();
					error("Invalid duplicate identifier!");
				}
				else {
					setPrevious(3);
					error("Strong can only not be applied to dynamic variable types!");
				}
			}
		}
		
		VariableStatement statement = new VariableStatement(declaration);
		if (name == null) { name = type; }
		
		//parse each variable
		do {
			name = (name == null) ? consume(IDENTIFIER, "Expected a variable name!") : name;
			
			Expression initializer = null;
			if (match(ASSIGN) || matchType(OPERATOR)) { initializer = expression(); }
			
			//add variable to statement
			statement.addVar(name, initializer);
			name = null;
		}
		while (match(COMMA));
		
		errorIf(!match(SEMICOLON, NEWLINE, EOF), "Incomplete variable declaration!");
		return statement;
	}
	
}
