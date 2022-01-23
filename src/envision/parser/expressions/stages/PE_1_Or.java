package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.LogicalExpression;
import envision.tokenizer.Token;

public class PE_1_Or extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_2_And.run();
		
		while (match(OR)) {
			Token operator = previous();
			Expression right = PE_2_And.run();
			e = new LogicalExpression(e, operator, right);
		}
		
		return e;
	}
	
}
