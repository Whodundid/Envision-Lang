package envision_lang.parser.expressions.expression_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.ExpressionHandler;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.tokenizer.Token;

public class Expr_Import extends ParsedExpression {
	
	//========
	// Fields
	//========
	
	public final Token<?> path;
	public final Token<?> object;
	
	//==============
	// Constructors
	//==============
	
	public Expr_Import(Token<?> pathIn, Token<?> objectIn) {
		super(pathIn);
		path = pathIn;
		object = objectIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String p = (path != null && path.getLexeme() != null) ? path.getLexeme() : "";
		String ip = (!p.isEmpty()) ? p + "." : "";
		return ip + object;
	}
	
	@Override
	public Expr_Import copy() {
		return new Expr_Import(path.copy(), object.copy());
	}
	
	@Override
	public EnvisionObject evaluate(ExpressionHandler handler) {
		return handler.handleImport_E(this);
	}

}