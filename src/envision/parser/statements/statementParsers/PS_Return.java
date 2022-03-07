package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.ReturnStatement;
import eutil.datatypes.EArrayList;

/**
 * Parses return statements and its conditional counterpart.
 * 
 * @author Hunter Bragg
 */
public class PS_Return extends GenericParser {
	
	/**
	 * Parses return statements and its conditional counterpart. Returns
	 * statements can have conditional logic in the form of 'retif' where
	 * the statement will only return if the given parsed condition is
	 * true.
	 * <p>
	 * Returns statements have the ability to return multiple values by
	 * wrapping individual arguments into a list.
	 * 
	 * @return The parsed return statement
	 */
	public static Statement returnStatement() {
		return returnStatement(false);
	}
	
	/**
	 * Parses return statements and its conditional counterpart. Returns
	 * statements can have conditional logic in the form of 'retif' where
	 * the statement will only return if the given parsed condition is
	 * true.
	 * <p>
	 * Returns statements have the ability to return multiple values by
	 * wrapping individual arguments into a list.
	 * 
	 * @return The parsed return statement
	 */
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
