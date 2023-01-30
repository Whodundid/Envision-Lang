package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_For;
import eutil.datatypes.util.EList;

public class IS_For extends AbstractInterpreterExecutor {

	public static void run(EnvisionInterpreter interpreter, Stmt_For statement) {
		ParsedStatement init = statement.init;
		ParsedStatement body = statement.body;
		ParsedExpression cond = statement.cond;
		EList<ParsedExpression> post = statement.post;
		
		interpreter.pushScope();
		
		//handle inits
		if (init != null) {
			interpreter.execute(init);
		}
		
		//true by default
		boolean conditionValue = true;
		if (cond != null) {
			conditionValue = isTrue(interpreter.evaluate(cond));
		}
		
		//body
		TOP:
		while (conditionValue) {
			interpreter.pushScope();
			if (body != null) {
				try {
					interpreter.execute(body);
				}
				catch (Continue c) {}
				catch (Break b) { break TOP; }
				catch (Exception e) { throw e; }
			}
			//post
			if (post != null) {
				for (ParsedExpression postExp : post) {
					if (postExp != null) {
						interpreter.evaluate(postExp);
					}
				}
			}
			interpreter.popScope();
			
			//re-evaluate condition
			if (cond != null) {
				conditionValue = isTrue(interpreter.evaluate(cond));
			}
		}
		
		interpreter.popScope();
	}
	
}
