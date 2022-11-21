package envision_lang.interpreter.statements;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.parser.statements.statement_types.Stmt_Generic;
import eutil.debug.Unused;

@Unused
public class IS_Generic extends StatementExecutor<Stmt_Generic> {

	public IS_Generic(EnvisionInterpreter in) {
		super(in);
	}

	public static void run(EnvisionInterpreter in, Stmt_Generic s) {
		new IS_Generic(in).run(s);
	}
	
	@Override
	public void run(Stmt_Generic statement) {
		
	}
	
}