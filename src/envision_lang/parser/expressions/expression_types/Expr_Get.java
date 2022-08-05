package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Get implements Expression {

	public final Expression object;
	public final Token name;
	
	public Expr_Get(Expression objectIn, Token nameIn) {
		object = objectIn;
		name = nameIn;
	}
	
	@Override
	public String toString() {
		return object + "." + name.lexeme;
	}
	
	@Override
	public Expr_Get copy() {
		return new Expr_Get(object.copy(), Token.copy(name));
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleGet_E(this);
	}
	
}
