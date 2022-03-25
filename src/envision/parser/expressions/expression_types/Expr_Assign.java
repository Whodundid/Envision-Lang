package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Operator;
import envision.tokenizer.Token;
import eutil.datatypes.util.BoxList;

public class Expr_Assign implements Expression {

	public Expr_Assign leftAssign;
	public Token name;
	public Operator operator;
	public Expression value;
	public final BoxList<Token, Token> modulars;
	
	public Expr_Assign(Token nameIn, Operator operatorIn, Expression valueIn) { this(nameIn, operatorIn, valueIn, null); }
	public Expr_Assign(Token nameIn, Operator operatorIn, Expression valueIn, BoxList<Token, Token> modularsIn) {
		name = nameIn;
		operator = operatorIn;
		value = valueIn;
		modulars = modularsIn;
	}
	
	public Expr_Assign(Expr_Assign left, Operator operatorIn, Expression valueIn) {
		leftAssign = left;
		name = null;
		operator = operatorIn;
		value = valueIn;
		modulars = null;
	}
	
	public String getName() {
		return name.lexeme;
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
	public Expr_Assign copy() {
		return new Expr_Assign(Token.copy(name), operator, value.copy(), modulars);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleAssign_E(this);
	}
	
}
