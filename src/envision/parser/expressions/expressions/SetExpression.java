package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class SetExpression implements Expression {

	public final Expression object, value;
	public final Token name;
	
	public SetExpression(Expression objectIn, Token nameIn, Expression valueIn) {
		object = objectIn;
		name = nameIn;
		value = valueIn;
	}
	
	@Override
	public String toString() {
		return object + "." + name + " = " + value;
	}
	
	@Override
	public SetExpression copy() {
		return new SetExpression(object.copy(), Token.copy(name), value.copy());
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleSet_E(this);
	}
	
}
