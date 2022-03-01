package envision.parser.statements.statementParsers;

import static envision.tokenizer.Operator.*;
import static envision.tokenizer.ReservedWord.*;

import envision.parser.GenericParser;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionParser;
import envision.parser.statements.Statement;
import envision.parser.statements.statements.LoopControlStatement;

public class PS_LoopControl extends GenericParser {
	
	public static Statement handleBreak() {
		if (match(BREAK)) { return new LoopControlStatement(true); }
		
		consume(BREAKIF, "Expected a 'breakif' statement!");
		consume(PAREN_L, "Expected the start of an expression! '('");
		Expression condition = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected the end of the given expression! ')'");
		
		return new LoopControlStatement(true, condition);
	}
	
	public static Statement handleContinue() {
		if (match(CONTINUE)) { return new LoopControlStatement(false); }
		
		consume(CONTIF, "Expected a 'contif' statement!");
		consume(PAREN_L, "Expected the start of an expression! '('");
		Expression condition = ExpressionParser.parseExpression();
		consume(PAREN_R, "Expected the end of the given expression! ')'");
		
		return new LoopControlStatement(false, condition);
	}
	
}
