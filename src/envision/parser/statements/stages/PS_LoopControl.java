package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.types.LoopControlStatement;

public class PS_LoopControl extends ParserStage {
	
	public static Statement handleBreak() {
		if (match(BREAK)) { return new LoopControlStatement(true); }
		
		consume(BREAKIF, "Expected a 'breakif' statement!");
		consume(EXPR_LEFT, "Expected the start of an expression! '('");
		Expression condition = expression();
		consume(EXPR_RIGHT, "Expected the end of the given expression! ')'");
		
		return new LoopControlStatement(true, condition);
	}
	
	public static Statement handleContinue() {
		if (match(CONTINUE)) { return new LoopControlStatement(false); }
		
		consume(CONTIF, "Expected a 'contif' statement!");
		consume(EXPR_LEFT, "Expected the start of an expression! '('");
		Expression condition = expression();
		consume(EXPR_RIGHT, "Expected the end of the given expression! ')'");
		
		return new LoopControlStatement(false, condition);
	}
	
}
