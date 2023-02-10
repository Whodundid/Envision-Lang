package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;

public class Expr_Ternary extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression condition, ifTrue, ifFalse;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Ternary(ParsedExpression conditionIn, ParsedExpression ifTrueIn, ParsedExpression ifFalseIn) {
		super(conditionIn);
		condition = conditionIn;
		ifTrue = ifTrueIn;
		ifFalse = ifFalseIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "(" + condition + ") ? " + ifTrue + " : " + ifFalse;
	}
	
	@Override
	public Expr_Ternary copy() {
		 return new Expr_Ternary(condition.copy(), ifTrue.copy(), ifFalse.copy());
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleTernary_E(this);
	}
	
}
