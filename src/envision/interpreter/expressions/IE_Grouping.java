package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.expression_types.Expr_Grouping;

public class IE_Grouping extends ExpressionExecutor<Expr_Grouping> {

	public IE_Grouping(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Grouping e) {
		return new IE_Grouping(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Grouping expression) {
		return evaluate(expression.expression);
	}
	
}