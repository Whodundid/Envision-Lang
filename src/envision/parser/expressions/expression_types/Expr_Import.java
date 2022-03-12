package envision.parser.expressions.expression_types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class Expr_Import implements Expression {

	public final String path;
	public final String object;
	
	public Expr_Import(String pathIn, String objectIn) {
		path = pathIn;
		object = objectIn;
	}
	
	@Override
	public String toString() {
		String p = (path != null && !path.isEmpty()) ? path + "." : "";
		return p + object;
	}
	
	@Override
	public Expr_Import copy() {
		return new Expr_Import(path, object);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleImport_E(this);
	}

}