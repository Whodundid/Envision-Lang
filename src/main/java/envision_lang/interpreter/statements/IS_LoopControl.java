package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.statement_types.Stmt_LoopControl;

public class IS_LoopControl extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_LoopControl s) {
		ParsedExpression condition = s.condition;
		
		//if there is a condition, evaluate condition and
		//only break/continue if true
		if (condition != null) {
			if (interpreter.isTrue(interpreter.evaluate(condition))) {
				if (s.isBreak) throw Break.instance;
				else throw Continue.instance;
			}
		}
		//if there's no condition, just break/continue
		else if (s.isBreak) throw Break.instance;
		else throw Continue.instance;
	}
	
}