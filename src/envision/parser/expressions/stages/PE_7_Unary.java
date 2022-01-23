package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.UnaryExpression;
import envision.tokenizer.Token;

public class PE_7_Unary extends ParserStage {
	
	public static Expression run() {
		if (check(NEGATE, SUBTRACT, DECREMENT, INCREMENT) && checkNext(IDENTIFIER)) {
			match(NEGATE, SUBTRACT, DECREMENT, INCREMENT);
			Token operator = previous();
			Expression right = PE_7_Unary.run();
			Expression e = new UnaryExpression(operator, right, null);
			return e;
		}
		
		Expression e = PE_8_Range.run();
		
		if (match(DECREMENT, INCREMENT)) {
			Token o = previous();
			e = new UnaryExpression(o, null, e);
		}
		
		return e;
	}
	
}
