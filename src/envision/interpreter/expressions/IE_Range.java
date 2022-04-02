package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.expression_types.Expr_Range;

public class IE_Range extends ExpressionExecutor<Expr_Range> {

	public IE_Range(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Range e) {
		return new IE_Range(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Range e) {
		EnvisionObject left = evaluate(e.left);
		EnvisionObject right = evaluate(e.right);
		EnvisionObject by = evaluate(e.by);
		
		System.out.println(left + " : " + right + " : " + by);
		
		return null;
	}
	
}