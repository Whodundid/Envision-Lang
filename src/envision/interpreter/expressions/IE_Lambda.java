package envision.interpreter.expressions;

import envision.exceptions.errors.InvalidTargetError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expressions.CompoundExpression;
import envision.parser.expressions.expressions.LambdaExpression;

public class IE_Lambda extends ExpressionExecutor<LambdaExpression> {

	public IE_Lambda(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(LambdaExpression e) {
		CompoundExpression inputs = e.inputs;
		CompoundExpression production = e.production;
		
		if (production.isEmpty()) throw new InvalidTargetError("Lambda Error: No production expression defined!");
		if (!production.hasOne()) throw new InvalidTargetError("A lambda expression can only define one production!");
			
		Object rVal = null;
		
		//push scope to isolate input expressions
		pushScope();
		for (Expression exp : inputs.expressions) evaluate(exp);
		rVal = evaluate(production.getFirst());
		popScope();
		
		return rVal;
	}
	
	public static Object run(EnvisionInterpreter in, LambdaExpression e) {
		return new IE_Lambda(in).run(e);
	}
	
}
