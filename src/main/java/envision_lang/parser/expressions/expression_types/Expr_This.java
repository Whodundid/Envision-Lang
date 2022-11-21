package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_This implements Expression {

	public final Token keyword;
	public final Token definingToken;
	
	public Expr_This(Token start) { this(start, null); }
	public Expr_This(Token start, Token keywordIn) {
		keyword = keywordIn;
		definingToken = start;
	}
	
	@Override
	public String toString() {
		String k = (keyword != null) ? "." + keyword.lexeme : "";
		return "this" + k;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleThisGet_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}
	
}
