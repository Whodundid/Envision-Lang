package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.objects.EnvisionList;
import envision.parser.expressions.expressions.ListInitializerExpression;

public class IE_ListInitializer extends ExpressionExecutor<ListInitializerExpression> {

	public IE_ListInitializer(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(ListInitializerExpression expression) {
		return new EnvisionList().addAll(expression.values.map(e -> evaluate(e)));
	}
	
	public static Object run(EnvisionInterpreter in, ListInitializerExpression e) {
		return new IE_ListInitializer(in).run(e);
	}
}
