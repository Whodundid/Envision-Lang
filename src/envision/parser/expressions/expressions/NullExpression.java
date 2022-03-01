package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class NullExpression implements Expression {
	
	@Override
	public String toString() {
		return "(null expression)";
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return null;
	}
	
}
