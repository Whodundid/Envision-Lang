package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_This implements Expression {

	public final Token keyword;
	
	public Expr_This() { this(null); }
	public Expr_This(Token keywordIn) {
		keyword = keywordIn;
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
	
}
