package envision_lang.interpreter.statements;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.parser.statements.statement_types.Stmt_SwitchCase;

public class IS_Case extends StatementExecutor<Stmt_SwitchCase> {

	public IS_Case(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_SwitchCase statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, Stmt_SwitchCase s) {
		new IS_Case(in).run(s);
	}
	
}