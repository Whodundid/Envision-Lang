package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Operator;
import envision_lang.tokenizer.Token;
import eutil.datatypes.BoxList;

public class Expr_Assign implements Expression {

	public Expr_Assign leftAssign;
	public Token name;
	public Operator operator;
	public Expression value;
	public final BoxList<Token, Token> modulars;
	public final Token definingToken;
	
	public Expr_Assign(Token nameIn, Operator operatorIn, Expression valueIn) { this(nameIn, operatorIn, valueIn, null); }
	public Expr_Assign(Token nameIn, Operator operatorIn, Expression valueIn, BoxList<Token, Token> modularsIn) {
		name = nameIn;
		operator = operatorIn;
		value = valueIn;
		modulars = modularsIn;
		definingToken = nameIn;
	}
	
	public Expr_Assign(Expr_Assign left, Operator operatorIn, Expression valueIn) {
		leftAssign = left;
		name = null;
		operator = operatorIn;
		value = valueIn;
		modulars = null;
		definingToken = left.definingToken;
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
		return n + " " + operator.typeString() + " " + value;
	}
	
	@Override
	public Expr_Assign copy() {
		return new Expr_Assign(name.copy(), operator, value.copy(), modulars);
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleAssign_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
