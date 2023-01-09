package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Range;

public class IE_Range extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Range expression) {
		EnvisionObject left = interpreter.evaluate(expression.left);
		EnvisionObject right = interpreter.evaluate(expression.right);
		EnvisionObject by = interpreter.evaluate(expression.by);
		
		System.out.println(left + " : " + right + " : " + by);
		
		return null;
	}
	
}