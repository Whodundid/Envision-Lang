package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Operator;

public class Expr_Logic extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression left, right;
	public final Operator operator;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Logic(ParsedExpression leftIn, Operator operatorIn, ParsedExpression rightIn) {
		super(leftIn);
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return left + " " + operator.operatorString + " " + right;
	}
	
	@Override
	public Expr_Logic copy() {
		return new Expr_Logic(left.copy(), operator, right.copy());
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleLogical_E(this);
	}
	
}
