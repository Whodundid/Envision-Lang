package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.expression_types.Expr_Generic;

public class IE_Generic extends ExpressionExecutor<Expr_Generic> {

	public IE_Generic(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Generic e) {
		return new IE_Generic(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Generic expression) {
		return null;
	}
	
}
