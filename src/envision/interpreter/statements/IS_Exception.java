package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.Stmt_Exception;
import eutil.debug.Broken;
import eutil.debug.InDevelopment;

@Broken
@InDevelopment
public class IS_Exception extends StatementExecutor<Stmt_Exception> {

	public IS_Exception(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Exception statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Exception s) {
		new IS_Exception(in).run(s);
	}
	
}