package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.BinaryExpression;
import envision.tokenizer.Token;

public class PE_5_Term extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_6_Factor.run();
		
		while (match(ADD, SUBTRACT)) {
			Token operator = previous();
			Expression right = PE_6_Factor.run();
			e = new BinaryExpression(e, operator, right);
		}
		
		return e;
	}
	
}
