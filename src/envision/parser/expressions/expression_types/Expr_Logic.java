package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Operator;

public class Expr_Logic implements Expression {

	public final Expression left, right;
	public final Operator operator;
	
	public Expr_Logic(Expression leftIn, Operator operatorIn, Expression rightIn) {
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
	}
	
	@Override
	public String toString() {
		return left + " " + operator.typeString + " " + right;
	}
	
	@Override
	public Expr_Logic copy() {
		return new Expr_Logic(left.copy(), operator, right.copy());
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleLogical_E(this);
	}
	
}
