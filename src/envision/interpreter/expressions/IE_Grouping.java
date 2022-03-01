package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expressions.GroupingExpression;

public class IE_Grouping extends ExpressionExecutor<GroupingExpression> {

	public IE_Grouping(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(GroupingExpression expression) {
		return evaluate(expression.expression);
	}
	
	public static Object run(EnvisionInterpreter in, GroupingExpression e) {
		return new IE_Grouping(in).run(e);
	}
	
}