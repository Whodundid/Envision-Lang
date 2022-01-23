package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class BinaryExpression implements Expression	{

	public final Expression left, right;
	public Token operator;
	public boolean modular;
	
	public BinaryExpression(Expression leftIn, Token operatorIn, Expression rightIn) { this(leftIn, operatorIn, rightIn, false); }
	public BinaryExpression(Expression leftIn, Token operatorIn, Expression rightIn, boolean modularIn) {
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
		modular = modularIn;
	}
	
	@Override
	public String toString() {
		String m = (modular) ? "~" : "";
		return left + " " + m + operator.lexeme + " " + right;
	}
	
	@Override
	public BinaryExpression copy() {
		return new BinaryExpression(left.copy(), Token.copy(operator), right.copy(), modular);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleBinary_E(this);
	}
	
}
