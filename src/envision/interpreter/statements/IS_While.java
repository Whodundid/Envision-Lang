package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.throwables.Break;
import envision.interpreter.util.throwables.Continue;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_Block;
import envision.parser.statements.statement_types.Stmt_While;

public class IS_While extends StatementExecutor<Stmt_While> {

	public IS_While(EnvisionInterpreter in) {
		super(in);
	}

	public static void run(EnvisionInterpreter in, Stmt_While s) {
		new IS_While(in).run(s);
	}
	
	@Override
	public void run(Stmt_While statement) {
		if (statement.isDo) {
			TOP:
			do {
				Statement b = statement.body;
				if (b == null) continue;
				
				pushScope();
				
				if (b instanceof Stmt_Block block) {
					for (Statement s : block.statements) {
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
			while (isTrue(evaluate(statement.condition)));
		}
		else {
			TOP:
			while (isTrue(evaluate(statement.condition))) {
				Statement b = statement.body;
				if (b == null) continue;
				
				pushScope();
				
				if (b instanceof Stmt_Block block) {
					for (Statement s : block.statements) {
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
