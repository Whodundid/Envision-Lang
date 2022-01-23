package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class LogicalExpression implements Expression {

	public final Expression left, right;
	public final Token operator;
	
	public LogicalExpression(Expression leftIn, Token operatorIn, Expression rightIn) {
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
	}
	
	@Override
	public String toString() {
		return left + " " + operator + " " + right;
	}
	
	@Override
	public LogicalExpression copy() {
		return new LogicalExpression(left.copy(), Token.copy(operator), right.copy());
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleLogical_E(this);
	}
	
}
