package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

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
	
}
