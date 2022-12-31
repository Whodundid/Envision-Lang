package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Range implements Expression {
	
	public final Expression left, right, by;
	public final Token<?> definingToken;
	
	public Expr_Range(Expression leftIn, Expression rightIn, Expression byIn) {
		left = leftIn;
		right = rightIn;
		by = byIn;
		definingToken = leftIn.definingToken();
	}
	
	@Override
	public String toString() {
		String b = (by != null) ? " by " + by : "";
		return left + " to " + right + b;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleRange_E(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
