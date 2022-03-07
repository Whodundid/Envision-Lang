package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.LiteralExpression;

public class IE_Literal extends ExpressionExecutor<LiteralExpression> {

	public IE_Literal(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(LiteralExpression expression) {
		//if (expression.value instanceof String) {
			//return EnvisionStringFormatter.stripQuotes((String) expression.value);
		//}
		return expression.value;
	}
	
	public static Object run(EnvisionInterpreter in, LiteralExpression e) {
		return new IE_Literal(in).run(e);
	}
	
}
