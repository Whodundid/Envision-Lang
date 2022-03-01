package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statements.CatchStatement;

public class IS_Catch extends StatementExecutor<CatchStatement> {

	public IS_Catch(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(CatchStatement statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, CatchStatement s) {
		new IS_Catch(in).run(s);
	}
	
}