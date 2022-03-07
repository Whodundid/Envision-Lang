package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class VarExpression implements Expression {

	public final Token name;
	
	public VarExpression(Token nameIn) {
		name = nameIn;
	}
	
	public String getName() {
		return (name != null) ? name.lexeme : null;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleVar_E(this);
	}
	
	@Override
	public VarExpression copy() {
		return new VarExpression(Token.copy(name));
	}
	
	@Override
	public String toString() {
		return name.lexeme + "";
	}
	
	public static VarExpression of(Token in) { return new VarExpression(in); }
	
}
