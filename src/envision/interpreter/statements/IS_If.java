package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.Stmt_If;

public class IS_If extends StatementExecutor<Stmt_If> {

	public IS_If(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_If statement) {
		if (isTruthy(evaluate(statement.condition))) {
			execute(statement.thenBranch);
		}
		else if (statement.elseBranch != null) { execute(statement.elseBranch); }
	}
	
	public static void run(EnvisionInterpreter in, Stmt_If s) {
		new IS_If(in).run(s);
	}
	
}
