package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Operator;
import envision.tokenizer.Token;
import eutil.datatypes.util.BoxList;

public class AssignExpression implements Expression {

	public AssignExpression leftAssign;
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
	
	public AssignExpression(AssignExpression left, Operator operatorIn, Expression valueIn) {
		leftAssign = left;
		name = null;
		operator = operatorIn;
		value = valueIn;
		modulars = null;
	}
	
	@Override
	public String toString() {
		String n = "";
		if (name != null) n = name.lexeme;
		else if (modulars != null) n = modulars + "";
		else n = leftAssign + "";
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
