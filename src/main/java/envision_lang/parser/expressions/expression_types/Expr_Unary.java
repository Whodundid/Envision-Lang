package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Operator;

public class Expr_Unary implements Expression {

	public final Operator operator;
	public final Expression right, left;
	
	public Expr_Unary(Operator operatorIn, Expression rightIn, Expression leftIn) {
		operator = operatorIn;
		right = rightIn;
		left = leftIn;
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
		return new Expr_Unary(operator, r, l);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleUnary_E(this);
	}
	
}
