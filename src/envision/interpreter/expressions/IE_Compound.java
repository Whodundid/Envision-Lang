package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.objects.EnvisionList;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.CompoundExpression;

public class IE_Compound extends ExpressionExecutor<CompoundExpression> {

	public IE_Compound(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(CompoundExpression expression) {
		EnvisionList l = new EnvisionList();
		for (Expression exp : expression.expressions) {
			l.add(evaluate(exp));
		}
		return l;
	}
	
	public static Object run(EnvisionInterpreter in, CompoundExpression e) {
		return new IE_Compound(in).run(e);
	}
	
}
