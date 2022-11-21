package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.tokenizer.Token;

public class Expr_Import implements Expression {

	public final Token path;
	public final Token object;
	public final Token definingToken;
	
	public Expr_Import(Token pathIn, Token objectIn) {
		path = pathIn;
		object = objectIn;
		definingToken = pathIn;
	}
	
	@Override
	public String toString() {
		String p = (path != null && path.lexeme != null) ? path.lexeme : "";
		String ip = (!p.isEmpty()) ? p + "." : "";
		return ip + object;
	}
	
	@Override
	public Expr_Import copy() {
		return new Expr_Import(path.copy(), object.copy());
	}
	
	@Override
	public EnvisionObject execute(ExpressionHandler handler) {
		return handler.handleImport_E(this);
	}
	
	@Override
	public Token definingToken() {
		return definingToken;
	}

}