package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Set implements Expression {

	public final Expression object, value;
	public final Token name;
	public final Token definingToken;
	
	public Expr_Set(Expression objectIn, Token nameIn, Expression valueIn) {
		object = objectIn;
		name = nameIn;
		value = valueIn;
		definingToken = objectIn.definingToken();
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
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
