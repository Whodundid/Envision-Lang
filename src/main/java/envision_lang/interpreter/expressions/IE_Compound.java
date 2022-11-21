package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.datatypes.EnvisionTupleClass;
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
		EnvisionTuple t = EnvisionTupleClass.newTuple();
		for (var exp : e.expressions) {
			t.getInternalList().add(evaluate(exp));
		}
		
		return t;
	}
	
}
