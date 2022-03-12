package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

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
	public Object execute(ExpressionHandler handler) {
		return handler.handleThisGet_E(this);
	}
	
}
