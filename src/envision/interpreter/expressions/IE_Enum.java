package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.EnumExpression;

public class IE_Enum extends ExpressionExecutor<EnumExpression> {

	public IE_Enum(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(EnumExpression expression) {
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, EnumExpression e) {
		return new IE_Enum(in).run(e);
	}
	
}
