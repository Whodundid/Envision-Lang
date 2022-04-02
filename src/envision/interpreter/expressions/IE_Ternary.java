package envision.interpreter.expressions;

import envision.exceptions.errors.NotABooleanError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBoolean;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_Ternary;

public class IE_Ternary extends ExpressionExecutor<Expr_Ternary> {

	public IE_Ternary(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Ternary e) {
		return new IE_Ternary(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Ternary expression) {
		Expression condition = expression.condition;
		Expression ifTrue = expression.ifTrue;
		Expression ifFalse = expression.ifFalse;
		
		EnvisionObject value = evaluate(condition);
		
		//check that the value is actually a boolean
		if (!(value instanceof EnvisionBoolean)) {
			throw new NotABooleanError(value);
		}
		
		return (isTrue(value)) ? evaluate(ifTrue) : evaluate(ifFalse);
	}
	
}