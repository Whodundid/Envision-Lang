package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_This extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Token<?> keyword;
	
	//==============
	// Constructors
	//==============
	
	public Expr_This(Token<?> start) { this(start, null); }
	public Expr_This(Token<?> start, Token<?> keywordIn) {
		super(start);
		keyword = keywordIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String k = (keyword != null) ? "." + keyword.getLexeme() : "";
		return "this" + k;
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleThisGet_E(this);
	}
	
}
