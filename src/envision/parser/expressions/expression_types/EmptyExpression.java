package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

/**
 * An empty expression.
 * Used as a placeholder.
 * 
 * @author Hunter
 */
public class EmptyExpression implements Expression {
	
	@Override
	public String toString() {
		return "(empty expression)";
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return null;
	}
	
}
