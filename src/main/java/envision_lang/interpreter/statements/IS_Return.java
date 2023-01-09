package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import eutil.datatypes.util.EList;

public class IS_Return extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_Return s) {
		ParsedExpression cond = s.condition;
		EList<ParsedExpression> r = s.retVals;
		
		//if there is a condition on the return, evaluate the condition and
		//only return a value if the condition is true
		if (cond != null && !isTrue(interpreter.evaluate(cond))) return;
		
		//otherwise, return some kind of value
		
		//if only returning one value
		if (r.hasOne()) {
			EnvisionObject env_obj = interpreter.evaluate(r.get(0));
			throw ReturnValue.wrap(env_obj);
		}
		//otherwise, wrap each value to return into a generic list
		else {
			EnvisionList retList = EnvisionListClass.newList();
			
			for (var o : r) {
				EnvisionObject env_obj = interpreter.evaluate(o);
				retList.add(env_obj);
			}
			
			retList.lockSize();
			
			throw ReturnValue.wrap(retList);
		}
	}
	
}