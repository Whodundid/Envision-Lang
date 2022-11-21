package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Get implements Expression {

	public final Expression object;
	public final Token name;
	public final Token definingToken;
	
	public Expr_Get(Expression objectIn, Token nameIn) {
		object = objectIn;
		name = nameIn;
		definingToken = objectIn.definingToken();
	}
	
	@Override
	public String toString() {
		return object + "." + name.lexeme;
	}
	
	@Override
	public Expr_Get copy() {
		return new Expr_Get(object.copy(), name.copy());
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleGet_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
