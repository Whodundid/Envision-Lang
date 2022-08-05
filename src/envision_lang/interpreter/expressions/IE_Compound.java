package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.parser.expressions.expression_types.Expr_Compound;

public class IE_Compound extends ExpressionExecutor<Expr_Compound> {

	public IE_Compound(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Compound e) {
		return new IE_Compound(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Compound e) {
		if (e.hasOne()) return evaluate(e.getFirst());
		
		//wrap into list
		EnvisionList l = EnvisionListClass.newList();
		for (var exp : e.expressions) {
			l.add(evaluate(exp));
		}
		
		return l;
	}
	
}
