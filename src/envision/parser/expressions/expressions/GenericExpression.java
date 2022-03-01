package envision.parser.expressions.expressions;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;
import envision.tokenizer.Token;

public class GenericExpression implements Expression	{
	
	public final Token generic;
	public final Token extension;
	
	public GenericExpression(Token genericIn, Token extensionIn) {
		generic = genericIn;
		extension = extensionIn;
	}
	
	@Override
	public String toString() {
		String g = (extension == null) ? "" : " extends " + extension.lexeme;
		return generic.lexeme + g;
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleGeneric_E(this);
	}
	
}
