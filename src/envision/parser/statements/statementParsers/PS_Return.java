package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statements.ReturnStatement;
import eutil.datatypes.EArrayList;

public class PS_Return extends GenericParser {
	
	public static Statement returnStatement() { return returnStatement(false); }
	public static Statement returnStatement(boolean parseForCondition) {
		while (match(NEWLINE));
		
		//handle condition stuff
		Expression cond = null;
		if (parseForCondition) {
			consume(PAREN_L, "Expected a '(' to begin if expression!");
			cond = ExpressionParser.parseExpression();
			consume(PAREN_R, "Expected a ')' to close if expression!");
		}
		
		//get return values
		EArrayList<Expression> retVals = new EArrayList();
		if (!check(SEMICOLON, NEWLINE)) {
			do {
				retVals.add(ExpressionParser.parseExpression());
			}
			while (match(COMMA));
		}
		
		return new ReturnStatement(cond, retVals);
	}
	
}
