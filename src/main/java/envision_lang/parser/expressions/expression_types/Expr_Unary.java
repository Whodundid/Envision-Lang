package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;

public class Expr_Unary implements Expression {

	public final Operator operator;
	public final Expression right, left;
	public final Token definingToken;
	
	public Expr_Unary(Token start, Operator operatorIn, Expression rightIn, Expression leftIn) {
		operator = operatorIn;
		right = rightIn;
		left = leftIn;
		definingToken = start;
	}
	
	@Override
	public String toString() {
		String r = (right != null) ? operator.typeString + right : left + operator.typeString;
		return r;
	}
	
	@Override
	public Expr_Unary copy() {
		Expression r = (right != null) ? right.copy() : null;
		Expression l = (left != null) ? left.copy() : null;
		return new Expr_Unary(definingToken.copy(), operator, r, l);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleUnary_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
