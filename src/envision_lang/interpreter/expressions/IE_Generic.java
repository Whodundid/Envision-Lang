package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Generic;

public class IE_Generic extends ExpressionExecutor<Expr_Generic> {

	public IE_Generic(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Generic e) {
		return new IE_Generic(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Generic expression) {
		//not implemented
		return null;
	}
	
}
