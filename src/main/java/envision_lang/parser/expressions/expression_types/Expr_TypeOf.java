package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_TypeOf implements Expression {
	
	public final Expression left, right;
	public final boolean is;
	public final Token definingToken;
	
	public Expr_TypeOf(Expression leftIn, boolean isIn, Expression rightIn) {
		left = leftIn;
		is = isIn;
		right = rightIn;
		definingToken = leftIn.definingToken();
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
