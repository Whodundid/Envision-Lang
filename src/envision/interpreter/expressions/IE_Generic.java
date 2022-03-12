package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_Generic;

public class IE_Generic extends ExpressionExecutor<Expr_Generic> {

	public IE_Generic(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Generic expression) {
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Generic e) {
		return new IE_Generic(in).run(e);
	}
	
}
