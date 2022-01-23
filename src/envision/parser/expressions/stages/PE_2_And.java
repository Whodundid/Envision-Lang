package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.LogicalExpression;
import envision.tokenizer.Token;

public class PE_2_And extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_3_Equality.run();
		
		while (match(AND)) {
			Token operator = previous();
			Expression right = equality();
			e = new LogicalExpression(e, operator, right);
		}
		
		return e;
	}
	
}
