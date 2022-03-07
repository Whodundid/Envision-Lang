package envision.interpreter.expressions;

import envision.exceptions.errors.NotABooleanError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.datatypes.EnvisionBoolean;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.TernaryExpression;

public class IE_Ternary extends ExpressionExecutor<TernaryExpression> {

	public IE_Ternary(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(TernaryExpression expression) {
		Expression condition = expression.condition;
		Expression ifTrue = expression.ifTrue;
		Expression ifFalse = expression.ifFalse;
		
		Object value = evaluate(condition);
		
		//check that the value is actually a boolean
		if (!(value instanceof Boolean || value instanceof EnvisionBoolean)) {
			throw new NotABooleanError(value);
		}
		
		return (isTruthy(value)) ? evaluate(ifTrue) : evaluate(ifFalse);
	}
	
	public static Object run(EnvisionInterpreter in, TernaryExpression e) {
		return new IE_Ternary(in).run(e);
	}
	
}