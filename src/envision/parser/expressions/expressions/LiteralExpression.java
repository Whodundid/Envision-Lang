package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class LiteralExpression implements Expression {
	
	public final Object value;
	
	public LiteralExpression(Object valueIn) {
		value = valueIn;
	}
	
	@Override
	public String toString() {
		return value + "";
	}
	
	@Override
	public LiteralExpression copy() {
		return new LiteralExpression(value);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleLiteral_E(this);
	}
	
}
