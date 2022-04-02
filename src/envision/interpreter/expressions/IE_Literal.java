package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.expression_types.Expr_Literal;

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
