package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_Domain;

public class IE_Domain extends ExpressionExecutor<Expr_Domain> {

	public IE_Domain(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Domain expression) {
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Domain e) {
		return new IE_Domain(in).run(e);
	}
	
}
