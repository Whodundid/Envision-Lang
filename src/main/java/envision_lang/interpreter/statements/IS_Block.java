package envision_lang.interpreter.statements;

import envision_lang.interpreter.CodeFileExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.parser.statements.statement_types.Stmt_Block;

public class IS_Block {
	
	public static void run(CodeFileExecutor executor, Stmt_Block s) {
		buildBlock(interpreter, statement, new Scope(scope()));
	}
	
	/** Executes block statements within the specified scope. */
	public static void buildBlock(Stmt_Block s, Scope scope) {
		executeBlock(s.statements, scope);
	}
	
}
