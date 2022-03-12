package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class Expr_Var implements Expression {

	public final Token name;
	
	public Expr_Var(Token nameIn) {
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
	public Expr_Var copy() {
		return new Expr_Var(Token.copy(name));
	}
	
	@Override
	public String toString() {
		return name.lexeme + "";
	}
	
	public static Expr_Var of(Token in) { return new Expr_Var(in); }
	
}
