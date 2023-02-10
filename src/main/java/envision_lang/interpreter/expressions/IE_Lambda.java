package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.error_types.InvalidTargetError;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Compound;
import envision_lang.parser.expressions.expression_types.Expr_Lambda;

public class IE_Lambda extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Lambda e) {
		Expr_Compound inputs = e.inputs;
		Expr_Compound production = e.production;
		
		if (production.isEmpty()) throw new InvalidTargetError("Lambda Error: No production expression defined!");
		//if (!production.hasOne()) throw new InvalidTargetError("A lambda expression can only define one production!");
			
		EnvisionObject rVal = null;
		
		//push scope to isolate input expressions
		interpreter.pushScope();
		for (ParsedExpression exp : inputs.expressions) interpreter.evaluate(exp);
		rVal = interpreter.evaluate(production);
		interpreter.popScope();
		
		return rVal;
	}
	
}
