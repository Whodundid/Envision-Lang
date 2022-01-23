package envision.parser.expressions.stages;

import static envision.tokenizer.Keyword.*;

import envision.parser.ParserStage;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.BinaryExpression;
import envision.parser.expressions.types.TypeOfExpression;
import envision.tokenizer.Token;

public class PE_3_Equality extends ParserStage {
	
	public static Expression run() {
		Expression e = PE_4_Lambda.run();
		
		if (match(NOT_EQUALS, COMPARE, GREATER_THAN, GREATER_THAN_EQUALS, LESS_THAN, LESS_THAN_EQUALS)) {
			Token operator = previous();
			Expression right = PE_4_Lambda.run();
			e = new BinaryExpression(e, operator, right);
		}
		
		//operator expression
		if (match(OPERATOR_EXPR)) {
			Token operator = null;
			if (check(IDENTIFIER)) operator = consume(IDENTIFIER, "Expected a valid name for the given operator!");
			if (ParserStage.modularValues != null && check(MODULAR_VALUE)) operator = consume(MODULAR_VALUE, "Expected a modular '@' reference for the given operator!");
			Expression right = PE_4_Lambda.run();
			e = new BinaryExpression(e, operator, right, true);
		}
		
		//typeof expressions
		if (match(IS, ISNOT)) {
			boolean is = previous().keyword == IS;
			Expression right = PE_4_Lambda.run();
			e = new TypeOfExpression(e, is, right);
		}
		
		return e;
	}
	
}
