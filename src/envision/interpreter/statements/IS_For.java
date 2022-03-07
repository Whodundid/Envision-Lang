package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.ForStatement;
import eutil.datatypes.EArrayList;

public class IS_For extends StatementExecutor<ForStatement> {

	public IS_For(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(ForStatement statement) {
		Statement init = statement.init;
		Statement body = statement.body;
		Expression cond = statement.cond;
		EArrayList<Expression> post = statement.post;
		
		pushScope();
		
		//inits
		execute(init);
		
		//body
		while (isTruthy(evaluate(cond))) {
			pushScope();
			execute(body);
			//post
			for (Expression postExp : post) {
				evaluate(postExp);
			}
			popScope();
		}
				
		popScope();
	}
	
	public static void run(EnvisionInterpreter in, ForStatement s) {
		new IS_For(in).run(s);
	}
	
}
