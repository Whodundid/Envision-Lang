package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

/**
 * An empty expression.
 * Used as a placeholder.
 * 
 * @author Hunter
 */
public class Expr_Cast implements Expression {
	
	public final Token toType;
	public final Expression target;
	
	public Expr_Cast(Token typeIn, Expression targetIn) {
		toType = typeIn;
		target = targetIn;
	}
	
	@Override
	public String toString() {
		return "(" + toType.lexeme + ") " + target;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleCast_E(this);
	}
	
}
