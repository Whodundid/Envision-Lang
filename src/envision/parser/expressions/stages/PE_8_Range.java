package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.RangeExpression;

public class PE_8_Range extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_9_MethodCall.run();
		
		if (!(e instanceof RangeExpression) && match(TO)) {
			Expression right = PE_8_Range.run();
			Expression by = null;
			if (match(BY)) {
				by = PE_8_Range.run();
			}
			e = new RangeExpression(e, right, by);
		}
		
		return e;
	}
	
}
