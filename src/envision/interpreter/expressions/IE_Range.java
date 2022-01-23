package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.types.RangeExpression;

public class IE_Range extends ExpressionExecutor<RangeExpression> {

	public IE_Range(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(RangeExpression e) {
		Object left = evaluate(e.left);
		Object right = evaluate(e.right);
		Object by = evaluate(e.by);
		
		System.out.println(left + " : " + right + " : " + by);
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, RangeExpression e) {
		return new IE_Range(in).run(e);
	}
	
}