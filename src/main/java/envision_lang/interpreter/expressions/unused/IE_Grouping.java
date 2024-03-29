package envision_lang.interpreter.expressions.unused;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.unused.Expr_Grouping;

public class IE_Grouping extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Grouping expression) {
		return interpreter.evaluate(expression.expression);
	}
	
}