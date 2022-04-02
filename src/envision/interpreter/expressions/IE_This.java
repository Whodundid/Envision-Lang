package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.expression_types.Expr_This;

public class IE_This extends ExpressionExecutor<Expr_This> {

	public IE_This(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_This e) {
		return new IE_This(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_This e) {
		return scope().get("this");
	}
	
}
