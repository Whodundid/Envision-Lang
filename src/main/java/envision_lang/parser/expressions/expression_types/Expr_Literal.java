package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;

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
