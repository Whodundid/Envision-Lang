package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Var implements Expression {

	public final Token<?> name;
	public final Token<?> definingToken;
	
	public Expr_Var(Token nameIn) {
		name = nameIn;
		definingToken = nameIn;
	}
	
	public String getName() {
		return (name != null) ? name.getLexeme() : null;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleVar_E(this);
	}
	
	@Override
	public Expr_Var copy() {
		return new Expr_Var(name.copy());
	}
	
	@Override
	public String toString() {
		return name.getLexeme() + "";
	}
	
	public static Expr_Var of(Token in) {
		return new Expr_Var(in);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
