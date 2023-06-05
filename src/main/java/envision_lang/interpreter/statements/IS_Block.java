package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.parser.statements.statement_types.Stmt_Block;

public class IS_Block extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_Block s) {
		IScope scope = new Scope(interpreter.scope());
		
		// executes block statements within the specified scope
		interpreter.executeBlock(s, scope);
	}
	
}
