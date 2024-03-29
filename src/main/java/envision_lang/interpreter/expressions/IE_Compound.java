package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.datatypes.EnvisionTupleClass;
import envision_lang.parser.expressions.expression_types.Expr_Compound;

public class IE_Compound extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Compound e) {
		if (e.hasOne()) return interpreter.evaluate(e.getFirst());
		
		// check for quick empty tuple return
		if (e.expressions.isEmpty()) return EnvisionTuple.EMPTY_TUPLE;
		
		// wrap into tuple
		EnvisionTuple t = EnvisionTupleClass.newTuple();
		int size = e.expressions.size();
		for (int i = 0; i < size; i++) {
			var exp = e.expressions.get(i);
			t.getInternalList().add(interpreter.evaluate(exp));
		}
		
		return t;
	}
	
}
