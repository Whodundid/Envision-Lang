package envision_lang.interpreter.statements;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_For;
import eutil.datatypes.EArrayList;

public class IS_For extends StatementExecutor<Stmt_For> {

	public IS_For(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_For s) {
		new IS_For(in).run(s);
	}

	@Override
	public void run(Stmt_For statement) {
		Statement init = statement.init;
		Statement body = statement.body;
		Expression cond = statement.cond;
		EArrayList<Expression> post = statement.post;
		
		pushScope();
		
		//inits
		execute(init);
		
		//body
		while (isTrue(evaluate(cond))) {
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
	
}
