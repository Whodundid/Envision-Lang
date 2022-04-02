package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.throwables.Break;
import envision.interpreter.util.throwables.Continue;
import envision.parser.expressions.Expression;
import envision.parser.statements.statement_types.Stmt_LoopControl;

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
				if (s.isBreak) throw new Break();
				else throw new Continue();
			}
		}
		//if there's no condition, just break/continue
		else if (s.isBreak) throw new Break();
		else throw new Continue();
	}
	
}