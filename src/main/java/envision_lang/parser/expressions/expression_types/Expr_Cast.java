package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

/**
 * An empty expression.
 * Used as a placeholder.
 * 
 * @author Hunter Bragg
 */
public class Expr_Cast implements Expression {
	
	public final Token toType;
	public final Expression target;
	public final Token definingToken;
	
	public Expr_Cast(Token typeIn, Expression targetIn) {
		toType = typeIn;
		target = targetIn;
		definingToken = typeIn;
	}
	
	@Override
	public String toString() {
		return "(" + toType.lexeme + ") " + target;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleCast_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
