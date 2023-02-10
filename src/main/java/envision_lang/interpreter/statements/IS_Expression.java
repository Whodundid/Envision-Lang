package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.parser.statements.statement_types.Stmt_Expression;

public class IS_Expression extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_Expression s) {
		evaluate(interpreter, s.expression);
	}
	
}