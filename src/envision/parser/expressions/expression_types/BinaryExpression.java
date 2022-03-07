package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Operator;

public class BinaryExpression implements Expression	{

	public final Expression left, right;
	public Operator operator;
	public boolean modular;
	
	public BinaryExpression(Expression leftIn, Operator operatorIn, Expression rightIn) { this(leftIn, operatorIn, rightIn, false); }
	public BinaryExpression(Expression leftIn, Operator operatorIn, Expression rightIn, boolean modularIn) {
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
	public BinaryExpression copy() {
		return new BinaryExpression(left.copy(), operator, right.copy(), modular);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleBinary_E(this);
	}
	
}
