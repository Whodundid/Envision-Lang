package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Literal extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Token<?> literalToken;
	public final Object value;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Literal(Token<?> start, Object valueIn) {
		super(start);
		literalToken = start;
		value = valueIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return value + "";
	}
	
	@Override
	public Expr_Literal copy() {
		return new Expr_Literal(getStartingToken(), value);
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleLiteral_E(this);
	}
	
}
