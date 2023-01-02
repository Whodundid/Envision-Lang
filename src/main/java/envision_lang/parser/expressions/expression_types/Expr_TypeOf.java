package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;

public class Expr_TypeOf extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression left, right;
	public final boolean is;
	
	//==============
	// Constructors
	//==============
	
	public Expr_TypeOf(ParsedExpression leftIn, boolean isIn, ParsedExpression rightIn) {
		super(leftIn);
		left = leftIn;
		is = isIn;
		right = rightIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String n = (is) ? " is " : " isnot ";
		return left + n + right;
	}

	@Override
	public Expr_TypeOf copy() {
		return new Expr_TypeOf(left.copy(), is, right.copy());
	}

	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleTypeOf_E(this);
	}
	
}
