package envision.interpreter.statements;

import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.scope.Scope;
import envision.parser.statements.statement_types.Stmt_Block;
import envision.interpreter.EnvisionInterpreter;

public class IS_Block extends StatementExecutor<Stmt_Block> {

	public IS_Block(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Block statement) {
		buildBlock(interpreter, statement, new Scope(scope()));
	}
	
	/** Excecutes block statements within the specified scope. */
	public static void buildBlock(EnvisionInterpreter in, Stmt_Block s, Scope scope) {
		in.executeBlock(s.statements, scope);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Block s) {
		new IS_Block(in).run(s);
	}
	
}
