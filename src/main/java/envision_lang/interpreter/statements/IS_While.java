package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_While;

public class IS_While extends AbstractInterpreterExecutor {
	
	//===============================================================================================
	
	public static void run(EnvisionInterpreter interpreter, Stmt_While statement) {
		if (statement.isDo) handleDoWhile(interpreter, statement);
		else handleWhile(interpreter, statement);
	}
	
	//===============================================================================================
	
	/**
	 * Performs the execution of a DO_WHILE loop.
	 * 
	 * @param interpreter
	 * @param statement
	 */
	private static void handleDoWhile(EnvisionInterpreter interpreter, Stmt_While statement) {
		TOP:
		do {
			ParsedStatement b = statement.body;
			if (b == null) continue;
			
			try {
				interpreter.pushScope();
				interpreter.execute(b);
			}
			catch (Break e) { break TOP; }
			catch (Continue e) { continue TOP; }
			finally {
				interpreter.popScope();
			}
		}
		while (isTrue(interpreter.evaluate(statement.condition)));
	}
	
	/**
	 * Performs the execution of a WHILE loop.
	 * 
	 * @param interpreter
	 * @param statement
	 */
	private static void handleWhile(EnvisionInterpreter interpreter, Stmt_While statement) {
		TOP:
		while (isTrue(interpreter.evaluate(statement.condition))) {
			ParsedStatement b = statement.body;
			if (b == null) continue;
			
			try {
				interpreter.pushScope();
				interpreter.executeNext(b);
			}
			catch (Break e) { break TOP; }
			catch (Continue e) { continue TOP; }
			finally {
				interpreter.popScope();
			}
		}
	}
	
}
