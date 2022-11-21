package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Grouping implements Expression {

	public final Expression expression;
	public final Token definingToken;
	
	public Expr_Grouping(Expression expressionIn) {
		expression = expressionIn;
		definingToken = expressionIn.definingToken();
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
