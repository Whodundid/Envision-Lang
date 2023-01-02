package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;

public class Expr_Range extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression left, right, by;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Range(ParsedExpression leftIn, ParsedExpression rightIn, ParsedExpression byIn) {
		super(leftIn);
		left = leftIn;
		right = rightIn;
		by = byIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String b = (by != null) ? " by " + by : "";
		return left + " to " + right + b;
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleRange_E(this);
	}
	
}
