package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class Expr_Set implements Expression {

	public final Expression object, value;
	public final Token name;
	
	public Expr_Set(Expression objectIn, Token nameIn, Expression valueIn) {
		object = objectIn;
		name = nameIn;
		value = valueIn;
	}
	
	@Override
	public String toString() {
		return object + "." + name + " = " + value;
	}
	
	@Override
	public Expr_Set copy() {
		return new Expr_Set(object.copy(), Token.copy(name), value.copy());
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleSet_E(this);
	}
	
}
