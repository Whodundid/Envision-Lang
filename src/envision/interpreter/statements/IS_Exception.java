package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.ExceptionStatement;

public class IS_Exception extends StatementExecutor<ExceptionStatement> {

	public IS_Exception(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(ExceptionStatement statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, ExceptionStatement s) {
		new IS_Exception(in).run(s);
	}
	
}