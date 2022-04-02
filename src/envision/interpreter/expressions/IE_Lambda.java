package envision.interpreter.expressions;

import envision.exceptions.errors.InvalidTargetError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_Compound;
import envision.parser.expressions.expression_types.Expr_Lambda;

public class IE_Lambda extends ExpressionExecutor<Expr_Lambda> {

	public IE_Lambda(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Lambda e) {
		return new IE_Lambda(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Lambda e) {
		Expr_Compound inputs = e.inputs;
		Expr_Compound production = e.production;
		
		if (production.isEmpty()) throw new InvalidTargetError("Lambda Error: No production expression defined!");
		if (!production.hasOne()) throw new InvalidTargetError("A lambda expression can only define one production!");
			
		EnvisionObject rVal = null;
		
		//push scope to isolate input expressions
		pushScope();
		for (Expression exp : inputs.expressions) evaluate(exp);
		rVal = evaluate(production.getFirst());
		popScope();
		
		return rVal;
	}
	
}
