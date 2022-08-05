package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;

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
