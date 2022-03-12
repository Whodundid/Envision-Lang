package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_FuncDef;

public class IE_FuncDef extends ExpressionExecutor<Expr_FuncDef> {

	public IE_FuncDef(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Void run(Expr_FuncDef expression) {
		interpreter.execute(expression.declaration);
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_FuncDef e) {
		return new IE_FuncDef(in).run(e);
	}
	
}
