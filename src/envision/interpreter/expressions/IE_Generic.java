package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expressions.GenericExpression;

public class IE_Generic extends ExpressionExecutor<GenericExpression> {

	public IE_Generic(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(GenericExpression expression) {
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, GenericExpression e) {
		return new IE_Generic(in).run(e);
	}
	
}
