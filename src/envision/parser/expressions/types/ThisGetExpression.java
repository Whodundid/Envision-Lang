package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class ThisGetExpression implements Expression {

	public final Token keyword;
	
	public ThisGetExpression() { this(null); }
	public ThisGetExpression(Token keywordIn) {
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
