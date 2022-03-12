package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_This;

public class IE_This extends ExpressionExecutor<Expr_This> {

	public IE_This(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_This e) {
		return scope().get("this");
	}
	
	public static Object run(EnvisionInterpreter in, Expr_This e) {
		return new IE_This(in).run(e);
	}
	
}
