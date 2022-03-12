package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.Stmt_Expression;

public class IS_Expression extends StatementExecutor<Stmt_Expression> {

	public IS_Expression(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(Stmt_Expression statement) {
		evaluate(statement.expression);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_Expression s) {
		new IS_Expression(in).run(s);
	}
	
}