package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.LoopControlStatement;

/**
 * Parses for break and continue statements and their conditional
 * counterparts.
 * 
 * @author Hunter Bragg
 */
public class PS_LoopControl extends GenericParser {
	
	/**
	 * Parses break statements. Break statements can have conditional
	 * logic in the form of 'breakif' where the statement will only break
	 * if the given parsed condition is true.
	 * 
	 * @return The parsed break statement.
	 */
	public static Statement handleBreak() {
		if (match(BREAK)) return new LoopControlStatement(true);
		
		consume(BREAKIF, "Expected a 'breakif' statement!");
		consume(PAREN_L, "Expected the start of an expression! '('");
		Expression condition = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected the end of the given expression! ')'");
		
		return new LoopControlStatement(true, condition);
	}
	
	/**
	 * Parses continue statements. Continue statements can have conditional
	 * logic in the form of 'contif' where the statement will only continue
	 * if the given parsed condition is true.
	 * 
	 * @return The parsed continue statement.
	 */
	public static Statement handleContinue() {
		if (match(CONTINUE)) return new LoopControlStatement(false);
		
		consume(CONTIF, "Expected a 'contif' statement!");
		consume(PAREN_L, "Expected the start of an expression! '('");
		Expression condition = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected the end of the given expression! ')'");
		
		return new LoopControlStatement(false, condition);
	}
	
}
