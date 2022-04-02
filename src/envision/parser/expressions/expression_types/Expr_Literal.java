package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class Expr_Literal implements Expression {
	
	public final Object value;
	
	public Expr_Literal(Object valueIn) {
		value = valueIn;
	}
	
	@Override
	public String toString() {
		return value + "";
	}
	
	@Override
	public Expr_Literal copy() {
		return new Expr_Literal(value);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleLiteral_E(this);
	}
	
}
