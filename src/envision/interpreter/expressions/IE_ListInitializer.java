package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.objects.EnvisionList;
import envision.parser.expressions.expression_types.Expr_ListInitializer;

public class IE_ListInitializer extends ExpressionExecutor<Expr_ListInitializer> {

	public IE_ListInitializer(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_ListInitializer expression) {
		return new EnvisionList().addAll(expression.values.map(e -> evaluate(e)));
	}
	
	public static Object run(EnvisionInterpreter in, Expr_ListInitializer e) {
		return new IE_ListInitializer(in).run(e);
	}
}
