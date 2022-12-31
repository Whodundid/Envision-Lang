package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public class Expr_Binary implements Expression	{

	public final Expression left, right;
	public Operator operator;
	public boolean modular;
	public final Token<?>  definingToken;
	
	public Expr_Binary(Expression leftIn, Operator operatorIn, Expression rightIn) { this(leftIn, operatorIn, rightIn, false); }
	public Expr_Binary(Expression leftIn, Operator operatorIn, Expression rightIn, boolean modularIn) {
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
		modular = modularIn;
		definingToken = leftIn.definingToken();
	}
	
	@Override
	public String toString() {
		String m = (modular) ? "~" : "";
		return left + " " + m + operator.typeString + " " + right;
	}
	
	@Override
	public Expr_Binary copy() {
		return new Expr_Binary(left.copy(), operator, right.copy(), modular);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleBinary_E(this);
	}
	
	@Override
	public Token<?>  definingToken() {
		return definingToken;
	}
	
}
