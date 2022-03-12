package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.objects.EnvisionList;
import envision.parser.expressions.expression_types.Expr_Compound;

public class IE_Compound extends ExpressionExecutor<Expr_Compound> {

	public IE_Compound(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Compound e) {
		if (e.hasOne()) return evaluate(e.getFirst());
		
		EnvisionList l = new EnvisionList();
		for (var exp : e.expressions) l.add(evaluate(exp));
		
		return l;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Compound e) {
		return new IE_Compound(in).run(e);
	}
	
}
