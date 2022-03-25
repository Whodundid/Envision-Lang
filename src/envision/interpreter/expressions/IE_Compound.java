package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.lang.util.EnvisionDatatype;
import envision.parser.expressions.expression_types.Expr_Compound;

public class IE_Compound extends ExpressionExecutor<Expr_Compound> {

	public IE_Compound(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(Expr_Compound e) {
		if (e.hasOne()) return evaluate(e.getFirst());
		
		EnvisionList l = EnvisionListClass.newList();
		for (var exp : e.expressions) {
			Object exp_result = evaluate(exp);
			if (exp_result instanceof EnvisionObject env_obj) l.add(env_obj);
			else {
				EnvisionDatatype type = EnvisionDatatype.dynamicallyDetermineType(exp_result);
				EnvisionObject res_obj = ObjectCreator.createObject(type, exp_result, false, false);
				l.add(res_obj);
			}
		}
		
		return l;
	}
	
	public static Object run(EnvisionInterpreter in, Expr_Compound e) {
		return new IE_Compound(in).run(e);
	}
	
}
