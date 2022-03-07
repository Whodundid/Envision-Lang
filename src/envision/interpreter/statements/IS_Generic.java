package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.GenericStatement;

public class IS_Generic extends StatementExecutor<GenericStatement> {

	public IS_Generic(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(GenericStatement statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, GenericStatement s) {
		new IS_Generic(in).run(s);
	}
	
}