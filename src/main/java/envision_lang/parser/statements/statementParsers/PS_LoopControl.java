package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import envision_lang.parser.GenericParser;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionParser;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_LoopControl;
import envision_lang.tokenizer.Token;

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
		if (match(BREAK)) return new Stmt_LoopControl(previous(), true);
		
		Token start = consume(BREAKIF, "Expected a 'breakif' statement!");
		consume(PAREN_L, "Expected the start of an expression! '('");
		Expression condition = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected the end of the given expression! ')'");
		
		return new Stmt_LoopControl(start, true, condition);
	}
	
	/**
	 * Parses continue statements. Continue statements can have conditional
	 * logic in the form of 'contif' where the statement will only continue
	 * if the given parsed condition is true.
	 * 
	 * @return The parsed continue statement.
	 */
	public static Statement handleContinue() {
		if (match(CONTINUE)) return new Stmt_LoopControl(previous(), false);
		
		Token start = consume(CONTIF, "Expected a 'contif' statement!");
		consume(PAREN_L, "Expected the start of an expression! '('");
		Expression condition = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected the end of the given expression! ')'");
		
		return new Stmt_LoopControl(start, false, condition);
	}
	
}
