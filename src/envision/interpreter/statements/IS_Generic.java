package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.Stmt_Generic;

public class IS_Generic extends StatementExecutor<Stmt_Generic> {

	public IS_Generic(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Generic statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Generic s) {
		new IS_Generic(in).run(s);
	}
	
}