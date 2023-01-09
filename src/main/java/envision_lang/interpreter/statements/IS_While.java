package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import envision_lang.parser.statements.statement_types.Stmt_While;

public class IS_While extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_While statement) {
		if (statement.isDo) {
			TOP:
			do {
				ParsedStatement b = statement.body;
				if (b == null) continue;
				
				interpreter.pushScope();
				
				if (b instanceof Stmt_Block block) {
					for (ParsedStatement s : block.statements) {
						try { interpreter.execute(s); }
						catch (Break e) { break TOP; }
						catch (Continue e) { continue TOP; }
					}
				}
				else {
					try { interpreter.execute(statement.body); }
					catch (Break e) { break TOP; }
					catch (Continue e) { continue TOP; }
				}
				
				interpreter.popScope();
			}
			while (isTrue(interpreter.evaluate(statement.condition)));
		}
		else {
			TOP:
			while (isTrue(interpreter.evaluate(statement.condition))) {
				ParsedStatement b = statement.body;
				if (b == null) continue;
				
				interpreter.pushScope();
				
				if (b instanceof Stmt_Block block) {
					for (ParsedStatement s : block.statements) {
						try { interpreter.execute(s); }
						catch (Break e) { break TOP; }
						catch (Continue e) { continue TOP; }
					}
				}
				else {
					try { interpreter.execute(statement.body); }
					catch (Break e) { break TOP; }
					catch (Continue e) { continue TOP; }
				}
				
				interpreter.popScope();
			}
		}
	}
	
}
