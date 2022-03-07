package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.DomainExpression;

public class IE_Domain extends ExpressionExecutor<DomainExpression> {

	public IE_Domain(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(DomainExpression expression) {
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, DomainExpression e) {
		return new IE_Domain(in).run(e);
	}
	
}
