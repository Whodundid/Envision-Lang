package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_Enum;

public class IE_Enum extends ExpressionExecutor<Expr_Enum> {

	public IE_Enum(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Enum expression) {
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Enum e) {
		return new IE_Enum(in).run(e);
	}
	
}
