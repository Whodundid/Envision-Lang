package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.IfStatement;

public class IS_If extends StatementExecutor<IfStatement> {

	public IS_If(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(IfStatement statement) {
		if (isTruthy(evaluate(statement.condition))) {
			execute(statement.thenBranch);
		}
		else if (statement.elseBranch != null) { execute(statement.elseBranch); }
	}
	
	public static void run(EnvisionInterpreter in, IfStatement s) {
		new IS_If(in).run(s);
	}
	
}
