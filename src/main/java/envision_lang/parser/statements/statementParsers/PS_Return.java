package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.GenericParser;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import envision_lang.tokenizer.Token;
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
		Token returnToken = getAdvance();
		while (match(NEWLINE));
		
		//handle condition stuff
		Expression cond = null;
		if (parseForCondition) {
			consume(PAREN_L, "Expected a '(' to begin if expression!");
			cond = ExpressionParser.parseExpression();
			consume(PAREN_R, "Expected a ')' to close if expression!");
		}
		
		System.out.println("HERE");
		
		//get return values
		EArrayList<Expression> retVals = new EArrayList<>();
		if (!check(SEMICOLON, NEWLINE)) {
			do {
				retVals.add(ExpressionParser.parseExpression());
			}
			while (match(COMMA));
		}
		
		return new Stmt_Return(returnToken, cond, retVals);
	}
	
}
