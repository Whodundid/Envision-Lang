package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class UnaryExpression implements Expression {

	public final Token operator;
	public final Expression right, left;
	
	public UnaryExpression(Token operatorIn, Expression rightIn, Expression leftIn) {
		operator = operatorIn;
		right = rightIn;
		left = leftIn;
	}
	
	@Override
	public String toString() {
		String r = (right != null) ? operator.lexeme + right : left + operator.lexeme;
		return r;
	}
	
	@Override
	public UnaryExpression copy() {
		Expression r = (right != null) ? right.copy() : null;
		Expression l = (left != null) ? left.copy() : null;
		return new UnaryExpression(Token.copy(operator), r, l);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleUnary_E(this);
	}
	
}
