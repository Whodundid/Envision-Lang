package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_Modular;

public class IE_Modular extends ExpressionExecutor<Expr_Modular> {

	public IE_Modular(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Modular expression) {
		System.out.println("returning: " + expression.modulars);
		return expression.modulars;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Modular e) {
		return new IE_Modular(in).run(e);
	}
	
}
