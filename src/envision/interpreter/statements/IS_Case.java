package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statements.CaseStatement;

public class IS_Case extends StatementExecutor<CaseStatement> {

	public IS_Case(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(CaseStatement statement) {
		
	}
	
	public static void run(EnvisionInterpreter in, CaseStatement s) {
		new IS_Case(in).run(s);
	}
	
}