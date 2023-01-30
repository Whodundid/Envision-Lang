package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.parser.expressions.expression_types.Expr_Import;
import eutil.debug.PotentiallyUnused;

@PotentiallyUnused(reason="I am not sure that import expressions are ever actually directly called..")
public class IE_Import extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Import e) {
		return EnvisionStringClass.valueOf(e.path);
	}
	
}
