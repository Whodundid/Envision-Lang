package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.ImportExpression;

public class IE_Import extends ExpressionExecutor<ImportExpression> {

	public IE_Import(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(ImportExpression e) {
		return e.path;
	}
	
	public static Object run(EnvisionInterpreter in, ImportExpression e) {
		return new IE_Import(in).run(e);
	}
	
}
