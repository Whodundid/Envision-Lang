package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Grouping;

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