package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.Token;

/**
 * A type of expression that wraps a primitive datatype.
 * 
 * @author Hunter Bragg
 */
public class Expr_Primitive implements Expression {
	
	public final IKeyword primitiveType;
	public final Token<?> definingToken;
	
	public Expr_Primitive(Token datatypeToken) {
		primitiveType = datatypeToken.getKeyword();
		definingToken = datatypeToken;
	}
	
	@Override
	public String toString() {
		return primitiveType.toString();
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handlePrimitive_E(this);
	}

	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
