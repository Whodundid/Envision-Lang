package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class Expr_Domain implements Expression	{

	public final Expression left, middle, right;
	public final Token lower, upper;
	
	public Expr_Domain(Expression leftIn, Token lowerIn, Expression middleIn, Token upperIn, Expression rightIn) {
		left = leftIn;
		lower = lowerIn;
		middle = middleIn;
		upper = upperIn;
		right = rightIn;
	}
	
	@Override
	public String toString() {
		return left + " " + lower.lexeme + " " + middle + " " + upper.lexeme + " " + right;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleDomain_E(this);
	}
	
}