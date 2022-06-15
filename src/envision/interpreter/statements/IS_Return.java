package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
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
		EArrayList<Expression> r = s.retVals;
		
		//if there is a condition on the return, evaluate the condition and
		//only return a value if the condition is true
		if (cond != null && !isTrue(evaluate(cond))) return;
		
		//otherwise, return some kind of value
		
		//if only returning one value
		if (r.hasOne()) {
			EnvisionObject env_obj = evaluate(r.get(0));
			throw ReturnValue.create(env_obj);
			//throw new ReturnValue(env_obj);
		}
		//otherwise, wrap each value to return into a generic list
		else {
			EnvisionList retList = EnvisionListClass.newList();
			
			for (var o : r) {
				EnvisionObject env_obj = evaluate(o);
				retList.add(env_obj);
			}
			throw ReturnValue.create(retList);
			//throw new ReturnValue(retList);
		}
	}
	
}