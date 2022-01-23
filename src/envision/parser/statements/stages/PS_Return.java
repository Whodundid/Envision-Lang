package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.types.ReturnStatement;
import eutil.datatypes.EArrayList;

public class PS_Return extends ParserStage {
	
	public static Statement returnStatement(boolean condition) {
		while (match(NEWLINE));
		
		//handle condition stuff
		Expression cond = null;
		if (condition) {
			consume(EXPR_LEFT, "Expected a '(' to begin if expression!");
			cond = expression();
			consume(EXPR_RIGHT, "Expected a ')' to close if expression!");
		}
		
		//get return values
		EArrayList<Expression> retVals = new EArrayList();
		if (!check(SEMICOLON)) {
			do {
				retVals.add(expression());
			}
			while (match(COMMA));
		}
		
		return new ReturnStatement(cond, retVals);
	}
	
}
