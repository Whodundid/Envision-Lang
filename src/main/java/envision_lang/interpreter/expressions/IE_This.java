package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_This;

public class IE_This extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_This e) {
		return in.scope().get("this");
	}
	
}
