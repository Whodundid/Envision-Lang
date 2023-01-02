package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.Token;

/**
 * A type of expression that wraps a primitive datatype.
 * 
 * @author Hunter Bragg
 */
public class Expr_Primitive extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final IKeyword primitiveType;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Primitive(Token<?> datatypeToken) {
		super(datatypeToken);
		primitiveType = datatypeToken.getKeyword();
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		return primitiveType.toString();
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handlePrimitive_E(this);
	}
	
}
