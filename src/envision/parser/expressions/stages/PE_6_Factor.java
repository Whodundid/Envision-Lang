package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.BinaryExpression;
import envision.tokenizer.Token;

public class PE_6_Factor extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_7_Unary.run();
		
		while (match(MULTIPLY, DIVIDE, MODULUS)) {
			Token operator = previous();
			Expression right = unary();
			e = new BinaryExpression(e, operator, right);
		}
		
		return e;
	}
	
}
