package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Domain implements Expression	{

	public final Expression left, middle, right;
	public final Token<?> lower, upper;
	public final Token<?> definingToken;
	
	public Expr_Domain(Expression leftIn, Token<?> lowerIn, Expression middleIn, Token<?> upperIn, Expression rightIn) {
		left = leftIn;
		lower = lowerIn;
		middle = middleIn;
		upper = upperIn;
		right = rightIn;
		definingToken = leftIn.definingToken();
	}
	
	@Override
	public String toString() {
		return left + " " + lower.getLexeme() + " " + middle + " " + upper.getLexeme() + " " + right;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleDomain_E(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}