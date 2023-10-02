package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Operator;

public class Expr_Binary extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final ParsedExpression left, right;
	public Operator operator;
	public boolean modular;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Binary(ParsedExpression leftIn, Operator operatorIn, ParsedExpression rightIn) {
		this(leftIn, operatorIn, rightIn, false);
	}
	
	public Expr_Binary(ParsedExpression leftIn, Operator operatorIn, ParsedExpression rightIn, boolean modularIn) {
		super(leftIn);
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
		modular = modularIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String m = (modular) ? "~" : "";
		return left + " " + m + operator.operatorString + " " + right;
	}
	
	@Override
	public Expr_Binary copy() {
		return new Expr_Binary(left.copy(), operator, right.copy(), modular);
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleBinary_E(this);
	}
	
}
