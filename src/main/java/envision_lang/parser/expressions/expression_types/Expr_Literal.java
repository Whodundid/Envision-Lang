package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Literal implements Expression {
	
	public final Object value;
	public final Token<?> definingToken;
	
	public Expr_Literal(Token<?> start, Object valueIn) {
		value = valueIn;
		definingToken = start;
	}
	
	@Override
	public String toString() {
		return value + "";
	}
	
	@Override
	public Expr_Literal copy() {
		return new Expr_Literal(definingToken.copy(), value);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleLiteral_E(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
