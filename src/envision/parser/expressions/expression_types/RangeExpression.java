package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class RangeExpression implements Expression {
	
	public final Expression left, right, by;
	
	public RangeExpression(Expression leftIn, Expression rightIn, Expression byIn) {
		left = leftIn;
		right = rightIn;
		by = byIn;
	}
	
	@Override
	public String toString() {
		String b = (by != null) ? " by " + by : "";
		return left + " to " + right + b;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleRange_E(this);
	}
	
}
