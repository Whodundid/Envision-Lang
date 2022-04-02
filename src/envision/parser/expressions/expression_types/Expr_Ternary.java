package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class Expr_Ternary implements Expression {
	
	public final Expression condition, ifTrue, ifFalse;
	
	public Expr_Ternary(Expression conditionIn, Expression ifTrueIn, Expression ifFalseIn) {
		condition = conditionIn;
		ifTrue = ifTrueIn;
		ifFalse = ifFalseIn;
	}
	
	@Override
	public String toString() {
		return "(" + condition + ") ? " + ifTrue + " : " + ifFalse;
	}
	
	@Override
	public Expr_Ternary copy() {
		 return new Expr_Ternary(condition.copy(), ifTrue.copy(), ifFalse.copy());
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleTernary_E(this);
	}
	
}
