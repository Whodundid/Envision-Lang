package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Generic implements Expression	{
	
	public final Token<?> generic;
	public final Token<?> extension;
	public final Token<?> definingToken;
	
	public Expr_Generic(Token genericIn, Token<?> extensionIn) {
		generic = genericIn;
		extension = extensionIn;
		definingToken = genericIn;
	}
	
	@Override
	public String toString() {
		String g = (extension == null) ? "" : " extends " + extension.getLexeme();
		return generic.getLexeme() + g;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleGeneric_E(this);
	}
	
	@Override
	public Token<?> definingToken() {
		return definingToken;
	}
	
}
