package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;
import eutil.datatypes.util.BoxHolder;

public class AssignExpression implements Expression {

	public Token name;
	public Token operator;
	public Expression value;
	public final BoxHolder<Token, Token> modulars;
	
	public AssignExpression(Token nameIn, Token operatorIn, Expression valueIn) { this(nameIn, operatorIn, valueIn, null); }
	public AssignExpression(Token nameIn, Token operatorIn, Expression valueIn, BoxHolder<Token, Token> modularsIn) {
		name = nameIn;
		operator = operatorIn;
		value = valueIn;
		modulars = modularsIn;
	}
	
	@Override
	public String toString() {
		String n = (name != null) ? name.lexeme : modulars + "";
		return n + " " + operator.lexeme + " " + value;
	}
	
	@Override
	public AssignExpression copy() {
		return new AssignExpression(Token.copy(name), Token.copy(operator), value.copy(), modulars);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleAssign_E(this);
	}
	
}
