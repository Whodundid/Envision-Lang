package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.FuncDefExpression;

public class IE_FuncDef extends ExpressionExecutor<FuncDefExpression> {

	public IE_FuncDef(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Void run(FuncDefExpression expression) {
		interpreter.execute(expression.declaration);
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, FuncDefExpression e) {
		return new IE_FuncDef(in).run(e);
	}
	
}
