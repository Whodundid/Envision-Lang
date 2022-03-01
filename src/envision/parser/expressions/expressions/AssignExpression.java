package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Operator;
import envision.tokenizer.Token;
import eutil.datatypes.util.BoxList;

public class AssignExpression implements Expression {

	public Token name;
	public Operator operator;
	public Expression value;
	public final BoxList<Token, Token> modulars;
	
	public AssignExpression(Token nameIn, Operator operatorIn, Expression valueIn) { this(nameIn, operatorIn, valueIn, null); }
	public AssignExpression(Token nameIn, Operator operatorIn, Expression valueIn, BoxList<Token, Token> modularsIn) {
		name = nameIn;
		operator = operatorIn;
		value = valueIn;
		modulars = modularsIn;
	}
	
	@Override
	public String toString() {
		String n = (name != null) ? name.lexeme : modulars + "";
		return n + " " + operator.chars() + " " + value;
	}
	
	@Override
	public AssignExpression copy() {
		return new AssignExpression(Token.copy(name), operator, value.copy(), modulars);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleAssign_E(this);
	}
	
}
