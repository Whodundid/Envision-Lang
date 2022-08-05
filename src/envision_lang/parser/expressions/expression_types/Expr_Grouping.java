package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;

public class Expr_Grouping implements Expression {

	public final Expression expression;
	
	public Expr_Grouping(Expression expressionIn) {
		expression = expressionIn;
	}
	
	@Override
	public String toString() {
		String e = (expression != null) ? expression.toString() : "";
		return "(" + e + ")";
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleGrouping_E(this);
	}
	
}
