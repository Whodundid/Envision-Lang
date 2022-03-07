package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class TypeOfExpression implements Expression {
	
	public final Expression left, right;
	public final boolean is;
	
	public TypeOfExpression(Expression leftIn, boolean isIn, Expression rightIn) {
		left = leftIn;
		is = isIn;
		right = rightIn;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleTypeOf_E(this);
	}
	
	@Override
	public TypeOfExpression copy() {
		return new TypeOfExpression(Expression.copy(left), is, Expression.copy(right));
	}
	
	@Override
	public String toString() {
		String n = (is) ? " is " : " isnot ";
		return left + n + right;
	}
	
}
