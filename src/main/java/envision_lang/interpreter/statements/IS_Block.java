package envision_lang.interpreter.statements;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.parser.statements.statement_types.Stmt_Block;

public class IS_Block extends StatementExecutor<Stmt_Block> {

	public IS_Block(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Block statement) {
		buildBlock(interpreter, statement, new Scope(scope()));
	}
	
	/** Executes block statements within the specified scope. */
	public static void buildBlock(EnvisionInterpreter in, Stmt_Block s, Scope scope) {
		in.executeBlock(s.statements, scope);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Block s) {
		new IS_Block(in).run(s);
	}
	
}
