package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public class Expr_Unary extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Operator operator;
	public final ParsedExpression right, left;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Unary(Token<?> start, Operator operatorIn, ParsedExpression rightIn, ParsedExpression leftIn) {
		super(start);
		operator = operatorIn;
		right = rightIn;
		left = leftIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String r = (right != null) ? operator.typeString + right : left + operator.typeString;
		return r;
	}
	
	@Override
	public Expr_Unary copy() {
		ParsedExpression r = (right != null) ? right.copy() : null;
		ParsedExpression l = (left != null) ? left.copy() : null;
		return new Expr_Unary(getStartingToken(), operator, r, l);
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleUnary_E(this);
	}
	
}
