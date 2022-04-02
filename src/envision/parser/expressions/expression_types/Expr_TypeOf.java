package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class Expr_TypeOf implements Expression {
	
	public final Expression left, right;
	public final boolean is;
	
	public Expr_TypeOf(Expression leftIn, boolean isIn, Expression rightIn) {
		left = leftIn;
		is = isIn;
		right = rightIn;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleTypeOf_E(this);
	}
	
	@Override
	public Expr_TypeOf copy() {
		return new Expr_TypeOf(Expression.copy(left), is, Expression.copy(right));
	}
	
	@Override
	public String toString() {
		String n = (is) ? " is " : " isnot ";
		return left + n + right;
	}
	
}
