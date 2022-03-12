package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.Stmt_Package;

public class IS_Package extends StatementExecutor<Stmt_Package> {

	public IS_Package(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Package statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Package s) {
		new IS_Package(in).run(s);
	}
	
}