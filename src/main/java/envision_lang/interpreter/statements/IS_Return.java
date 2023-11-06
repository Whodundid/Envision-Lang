package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.EnvisionStringFormatter;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.datatypes.EnvisionTupleClass;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.statement_types.Stmt_Return;
import eutil.datatypes.util.EList;

public class IS_Return extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_Return s) {
		ParsedExpression cond = s.condition;
		EList<ParsedExpression> r = s.retVals;
		
		// if there is a condition on the return, evaluate the condition and
		// only return a value if the condition is true
		if (cond != null && !isTrue(interpreter.evaluate(cond))) return;
		
		// otherwise, return some kind of value
		
		// if only returning one value
		if (r.hasOne()) {
			EnvisionObject env_obj = interpreter.evaluate(r.get(0));
			if (env_obj instanceof EnvisionString) {
			    String formattedString = EnvisionStringFormatter.formatPrint(interpreter, env_obj);
			    EnvisionString str = EnvisionStringClass.valueOf(formattedString);
			    throw ReturnValue.wrap(str);
			}
			throw ReturnValue.wrap(env_obj);
		}
		// otherwise, wrap each value to return into a generic list
		else {
			EnvisionTuple retList = EnvisionTupleClass.newTuple();
			
			for (var o : r) {
				EnvisionObject env_obj = interpreter.evaluate(o);
				retList.add(env_obj);
			}
			
			throw ReturnValue.wrap(retList);
		}
	}
	
}