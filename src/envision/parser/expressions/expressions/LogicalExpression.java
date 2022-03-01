package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Operator;

public class LogicalExpression implements Expression {

	public final Expression left, right;
	public final Operator operator;
	
	public LogicalExpression(Expression leftIn, Operator operatorIn, Expression rightIn) {
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
	}
	
	@Override
	public String toString() {
		return left + " " + operator.chars + " " + right;
	}
	
	@Override
	public LogicalExpression copy() {
		return new LogicalExpression(left.copy(), operator, right.copy());
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleLogical_E(this);
	}
	
}
