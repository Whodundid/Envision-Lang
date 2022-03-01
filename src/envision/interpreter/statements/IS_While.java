package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.throwables.Break;
import envision.interpreter.util.throwables.Continue;
import envision.parser.statements.Statement;
import envision.parser.statements.statements.BlockStatement;
import envision.parser.statements.statements.WhileStatement;

public class IS_While extends StatementExecutor<WhileStatement> {

	public IS_While(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(WhileStatement statement) {
		if (statement.isDo) {
			TOP:
			do {
				Statement b = statement.body;
				if (b != null) {
					pushScope();
					
					if (b instanceof BlockStatement) {
						for (Statement s : ((BlockStatement) b).statements) {
							try { execute(s); }
							catch (Break e) { break TOP; }
							catch (Continue e) { continue TOP; }
						}
					}
					else {
						try { execute(statement.body); }
						catch (Break e) { break TOP; }
						catch (Continue e) { continue TOP; }
					}
					
					popScope();
				}
			}
			while (isTruthy(evaluate(statement.condition)));
		}
		else {
			TOP:
			while (isTruthy(evaluate(statement.condition))) {
				Statement b = statement.body;
				if (b != null) {
					pushScope();
					
					if (b instanceof BlockStatement) {
						for (Statement s : ((BlockStatement) b).statements) {
							try { execute(s); }
							catch (Break e) { break TOP; }
							catch (Continue e) { continue TOP; }
						}
					}
					else {
						try { execute(statement.body); }
						catch (Break e) { break TOP; }
						catch (Continue e) { continue TOP; }
					}
					
					popScope();
				}
			}
		}
	}
	
	public static void run(EnvisionInterpreter in, WhileStatement s) {
		new IS_While(in).run(s);
	}
	
}
