package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.parser.expressions.expression_types.Expr_Import;

public class IE_Import extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Import e) {
		return EnvisionStringClass.newString(e.path);
	}
	
}
