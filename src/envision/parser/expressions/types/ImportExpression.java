package envision.parser.expressions.types;

import envision.parser.expressions.Expression;
import envision.parser.expressions.ExpressionHandler;

public class ImportExpression implements Expression {

	public final String path;
	public final String object;
	
	public ImportExpression(String pathIn, String objectIn) {
		path = pathIn;
		object = objectIn;
	}
	
	@Override
	public String toString() {
		String p = (path != null && !path.isEmpty()) ? path + "." : "";
		return p + object;
	}
	
	@Override
	public ImportExpression copy() {
		return new ImportExpression(path, object);
	}
	
	@Override
	public Object execute(ExpressionHandler handler) {
		return handler.handleImport_E(this);
	}

}