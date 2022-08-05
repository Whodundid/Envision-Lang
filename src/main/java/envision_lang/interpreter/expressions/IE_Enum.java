package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Enum;

public class IE_Enum extends ExpressionExecutor<Expr_Enum> {

	public IE_Enum(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Enum e) {
		return new IE_Enum(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Enum expression) {
		//not implemented
		return null;
	}
	
}
