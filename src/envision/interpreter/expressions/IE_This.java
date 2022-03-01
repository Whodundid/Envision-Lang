package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expressions.ThisGetExpression;

public class IE_This extends ExpressionExecutor<ThisGetExpression> {

	public IE_This(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(ThisGetExpression e) {
		return scope().get("this");
	}
	
	public static Object run(EnvisionInterpreter in, ThisGetExpression e) {
		return new IE_This(in).run(e);
	}
	
}
