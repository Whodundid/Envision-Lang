package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_Import;

public class IE_Import extends ExpressionExecutor<Expr_Import> {

	public IE_Import(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Import e) {
		return e.path;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Import e) {
		return new IE_Import(in).run(e);
	}
	
}
