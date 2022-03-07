package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.objects.EnvisionList;
import envision.parser.expressions.Expression;
import envision.parser.statements.statement_types.ReturnStatement;
import eutil.datatypes.EArrayList;

public class IS_Return extends StatementExecutor<ReturnStatement> {

	public IS_Return(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(ReturnStatement s) {
		Expression cond = s.condition;
		Object r = s.retVals;
		
		//if there is a condition on the return, evaluate the condition and
		//only return a value if the condition is true
		if (cond != null && !isTruthy(evaluate(cond))) return;
		
		//otherwise, return some kind of value
		if (r instanceof EArrayList<?>) {
			EArrayList<?> retVals = (EArrayList<?>) r;
			
			if (retVals.hasOne()) {
				Object o = retVals.get(0);
				if (o instanceof Expression) throw new ReturnValue(evaluate((Expression) o));
			}
			else {
				EnvisionList retList = new EnvisionList();
				for (Object o : retVals) {
					if (o instanceof Expression) retList.add(evaluate((Expression) o));
				}
				throw new ReturnValue(retList);
			}
		}
	}
	
	public static void run(EnvisionInterpreter in, ReturnStatement s) {
		new IS_Return(in).run(s);
	}
	
}