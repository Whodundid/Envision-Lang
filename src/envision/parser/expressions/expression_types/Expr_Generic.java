package envision.parser.expressions.expression_types;

import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class Expr_Generic implements Expression	{
	
	public final Token generic;
	public final Token extension;
	
	public Expr_Generic(Token genericIn, Token extensionIn) {
		generic = genericIn;
		extension = extensionIn;
	}
	
	@Override
	public String toString() {
		String g = (extension == null) ? "" : " extends " + extension.lexeme;
		return generic.lexeme + g;
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleGeneric_E(this);
	}
	
}
