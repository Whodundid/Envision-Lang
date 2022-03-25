package envision.interpreter.statements;

import envision.exceptions.EnvisionError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.parser.expressions.Expression;
import envision.parser.statements.statement_types.Stmt_Return;
import eutil.datatypes.EArrayList;

public class IS_Return extends StatementExecutor<Stmt_Return> {

	public IS_Return(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Return s) {
		new IS_Return(in).run(s);
	}

	@Override
	public void run(Stmt_Return s) {
		Expression cond = s.condition;
		Object r = s.retVals;
		
		//if there is a condition on the return, evaluate the condition and
		//only return a value if the condition is true
		if (cond != null && !isTruthy(evaluate(cond))) return;
		
		//otherwise, return some kind of value
		if (r instanceof EArrayList<?>) {
			EArrayList<?> retVals = (EArrayList<?>) r;
			
			//if only returning one value
			if (retVals.hasOne()) {
				//ensure the retVal is actually an expression
				if (retVals.get(0) instanceof Expression expr) {
					Object result = evaluate(expr);
					EnvisionObject env_obj = ObjectCreator.wrap(result);
					throw new ReturnValue(env_obj);
				}
				else throw new EnvisionError("Not an expression! Cannot return this!");
			}
			//otherwise, wrap each value to return into a generic list
			else {
				EnvisionList retList = EnvisionListClass.newList();
				
				for (Object o : retVals) {
					//ensure the retVal is actually an expression
					if (o instanceof Expression expr) {
						Object result = evaluate(expr);
						EnvisionObject env_obj = ObjectCreator.wrap(result);
						retList.add(env_obj);
					}
					else throw new EnvisionError("Not an expression! Cannot return this!");
				}
				
				throw new ReturnValue(retList);
			}
		}
	}
	
}