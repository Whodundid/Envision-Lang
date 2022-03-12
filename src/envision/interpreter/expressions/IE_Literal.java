package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_Literal;

public class IE_Literal extends ExpressionExecutor<Expr_Literal> {

	public IE_Literal(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Literal expression) {
		//if (expression.value instanceof String) {
			//return EnvisionStringFormatter.stripQuotes((String) expression.value);
		//}
		return expression.value;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Literal e) {
		return new IE_Literal(in).run(e);
	}
	
}
