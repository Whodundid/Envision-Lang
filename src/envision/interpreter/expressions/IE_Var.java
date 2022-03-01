package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expressions.VarExpression;

public class IE_Var extends ExpressionExecutor<VarExpression> {

	public IE_Var(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(VarExpression expression) {
		return lookUpVariable(expression.name/*, expression*/);
	}
	
	public static Object run(EnvisionInterpreter in, VarExpression e) {
		return new IE_Var(in).run(e);
	}
	
}
