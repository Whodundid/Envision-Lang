package envision_lang.parser.expressions.expression_types.unused;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Generic extends ParsedExpression	{
	
	//========
	// Fields
	//========
	
	public final Token<?> generic;
	public final Token<?> extension;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Generic(Token genericIn, Token<?> extensionIn) {
		super(genericIn);
		generic = genericIn;
		extension = extensionIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String g = (extension == null) ? "" : " extends " + extension.getLexeme();
		return generic.getLexeme() + g;
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return null;
//		return handler.handleGeneric_E(this);
	}
	
}
