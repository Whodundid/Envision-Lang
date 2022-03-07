package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Operator;

public class UnaryExpression implements Expression {

	public final Operator operator;
	public final Expression right, left;
	
	public UnaryExpression(Operator operatorIn, Expression rightIn, Expression leftIn) {
		operator = operatorIn;
		right = rightIn;
		left = leftIn;
	}
	
	@Override
	public String toString() {
		String r = (right != null) ? operator.chars + right : left + operator.chars;
		return r;
	}
	
	@Override
	public UnaryExpression copy() {
		Expression r = (right != null) ? right.copy() : null;
		Expression l = (left != null) ? left.copy() : null;
		return new UnaryExpression(operator, r, l);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleUnary_E(this);
	}
	
}
