package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class TernaryExpression implements Expression {
	
	public final Expression condition, ifTrue, ifFalse;
	
	public TernaryExpression(Expression conditionIn, Expression ifTrueIn, Expression ifFalseIn) {
		condition = conditionIn;
		ifTrue = ifTrueIn;
		ifFalse = ifFalseIn;
	}
	
	@Override
	public String toString() {
		return "(" + condition + ") ? " + ifTrue + " : " + ifFalse;
	}
	
	@Override
	public TernaryExpression copy() {
		 return new TernaryExpression(condition.copy(), ifTrue.copy(), ifFalse.copy());
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleTernary_E(this);
	}
	
}
