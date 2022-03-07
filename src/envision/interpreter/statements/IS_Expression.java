package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.statements.statement_types.ExpressionStatement;

public class IS_Expression extends StatementExecutor<ExpressionStatement> {

	public IS_Expression(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(ExpressionStatement statement) {
		evaluate(statement.expression);
	}
	
	public static void run(EnvisionInterpreter in, ExpressionStatement s) {
		new IS_Expression(in).run(s);
	}
	
}