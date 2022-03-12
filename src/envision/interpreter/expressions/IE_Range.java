package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_Range;

public class IE_Range extends ExpressionExecutor<Expr_Range> {

	public IE_Range(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Range e) {
		Object left = evaluate(e.left);
		Object right = evaluate(e.right);
		Object by = evaluate(e.by);
		
		System.out.println(left + " : " + right + " : " + by);
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Range e) {
		return new IE_Range(in).run(e);
	}
	
}