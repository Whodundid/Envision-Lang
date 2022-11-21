package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

/**
 * An empty expression.
 * Used as a placeholder.
 * 
 * @author Hunter
 */
public class Expr_Empty implements Expression {
	
	@Override
	public String toString() {
		return "(empty expression)";
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return null;
	}

	@Override
	public Token definingToken() {
		return null;
	}
	
}
