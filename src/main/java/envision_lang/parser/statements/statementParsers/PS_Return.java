package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.ParserHead;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

/**
 * Parses return statements and its conditional counterpart.
 * 
 * @author Hunter Bragg
 */
public class PS_Return extends ParserHead {
	
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
	public static ParsedStatement returnStatement() {
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
	public static ParsedStatement returnStatement(boolean parseForCondition) {
		Token<?> returnToken;
		if (check(LAMBDA, RETURN)) returnToken = getAdvance();
		else returnToken = previous();
		
//		consumeEmptyLines();
		
		//handle condition stuff
		ParsedExpression cond = null;
		if (parseForCondition) {
			consume(PAREN_L, "Expected a '(' to begin if expression!");
			cond = ExpressionParser.parseExpression();
			consume(PAREN_R, "Expected a ')' to close if expression!");
		}
		
		//get return values
		EList<ParsedExpression> retVals = EList.newList();
		if (!check(SEMICOLON, NEWLINE)) {
			do {
				retVals.add(ExpressionParser.parseExpression());
//				consumeEmptyLines();
			}
			while (match(COMMA));
		}
		
		return new Stmt_Return(returnToken, cond, retVals);
	}
	
}
