package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Operator;

public class Expr_Binary implements Expression	{

	public final Expression left, right;
	public Operator operator;
	public boolean modular;
	
	public Expr_Binary(Expression leftIn, Operator operatorIn, Expression rightIn) { this(leftIn, operatorIn, rightIn, false); }
	public Expr_Binary(Expression leftIn, Operator operatorIn, Expression rightIn, boolean modularIn) {
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
		modular = modularIn;
	}
	
	@Override
	public String toString() {
		String m = (modular) ? "~" : "";
		return left + " " + m + operator.chars + " " + right;
	}
	
	@Override
	public Expr_Binary copy() {
		return new Expr_Binary(left.copy(), operator, right.copy(), modular);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleBinary_E(this);
	}
	
}
