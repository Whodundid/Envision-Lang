package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public class Expr_Logic implements Expression {

	public final Expression left, right;
	public final Operator operator;
	public final Token definingToken;
	
	public Expr_Logic(Expression leftIn, Operator operatorIn, Expression rightIn) {
		left = leftIn;
		operator = operatorIn;
		right = rightIn;
		definingToken = leftIn.definingToken();
	}
	
	@Override
	public String toString() {
		return left + " " + operator.typeString + " " + right;
	}
	
	@Override
	public Expr_Logic copy() {
		return new Expr_Logic(left.copy(), operator, right.copy());
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleLogical_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
