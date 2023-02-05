package envision_lang.parser.expressions.expression_types.unused;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

/**
 * 
 * @author Hunter Bragg
 */
public class Expr_Cast extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Token<?> toType;
	public final ParsedExpression target;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Cast(Token<?> typeIn, ParsedExpression targetIn) {
		super(typeIn);
		toType = typeIn;
		target = targetIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return "(" + toType.getLexeme() + ") " + target;
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return null;
//		return handler.handleCast_E(this);
	}
	
}
