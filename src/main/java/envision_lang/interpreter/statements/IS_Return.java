package envision_lang.interpreter.statements;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import eutil.datatypes.util.EList;

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
		EList<Expression> r = s.retVals;
		
		//if there is a condition on the return, evaluate the condition and
		//only return a value if the condition is true
		if (cond != null && !isTrue(evaluate(cond))) return;
		
		//otherwise, return some kind of value
		
		//if only returning one value
		if (r.hasOne()) {
			EnvisionObject env_obj = evaluate(r.get(0));
			throw ReturnValue.wrap(env_obj);
		}
		//otherwise, wrap each value to return into a generic list
		else {
			EnvisionList retList = EnvisionListClass.newList();
			
			for (var o : r) {
				EnvisionObject env_obj = evaluate(o);
				retList.add(env_obj);
			}
			
			retList.lockSize();
			
			throw ReturnValue.wrap(retList);
		}
	}
	
}