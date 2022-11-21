package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Ternary implements Expression {
	
	public final Expression condition, ifTrue, ifFalse;
	public final Token definingToken;
	
	public Expr_Ternary(Expression conditionIn, Expression ifTrueIn, Expression ifFalseIn) {
		condition = conditionIn;
		ifTrue = ifTrueIn;
		ifFalse = ifFalseIn;
		definingToken = condition.definingToken();
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
