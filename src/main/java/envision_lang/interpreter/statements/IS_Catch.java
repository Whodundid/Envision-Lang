package envision_lang.interpreter.statements;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.parser.statements.statement_types.Stmt_Catch;

public class IS_Catch extends StatementExecutor<Stmt_Catch> {

	public IS_Catch(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Catch statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Catch s) {
		new IS_Catch(in).run(s);
	}
	
}