package envision_lang.interpreter.statements;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.statement_types.Stmt_LoopControl;

public class IS_LoopControl extends StatementExecutor<Stmt_LoopControl> {

	public IS_LoopControl(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_LoopControl s) {
		new IS_LoopControl(in).run(s);
	}

	@Override
	public void run(Stmt_LoopControl s) {
		Expression condition = s.condition;
		
		//if there is a condition, evaluate condition and
		//only break/continue if true
		if (condition != null) {
			if (isTrue(evaluate(condition))) {
				if (s.isBreak) throw Break.instance;
				else throw Continue.instance;
			}
		}
		//if there's no condition, just break/continue
		else if (s.isBreak) throw Break.instance;
		else throw Continue.instance;
	}
	
}