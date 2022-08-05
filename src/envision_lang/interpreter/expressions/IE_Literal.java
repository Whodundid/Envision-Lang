package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Literal;

public class IE_Literal extends ExpressionExecutor<Expr_Literal> {

	public IE_Literal(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Literal e) {
		return new IE_Literal(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Literal expression) {
		return ObjectCreator.wrap(expression.value);
	}
	
}
