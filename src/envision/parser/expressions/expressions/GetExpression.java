package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class GetExpression implements Expression {

	public final Expression object;
	public final Token name;
	
	public GetExpression(Expression objectIn, Token nameIn) {
		object = objectIn;
		name = nameIn;
	}
	
	@Override
	public String toString() {
		return object + "." + name.lexeme;
	}
	
	@Override
	public GetExpression copy() {
		return new GetExpression(object.copy(), Token.copy(name));
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleGet_E(this);
	}
	
}
