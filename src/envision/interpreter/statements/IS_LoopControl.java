package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.throwables.Break;
import envision.interpreter.util.throwables.Continue;
import envision.parser.expressions.Expression;
import envision.parser.statements.statement_types.LoopControlStatement;

public class IS_LoopControl extends StatementExecutor<LoopControlStatement> {

	public IS_LoopControl(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public void run(LoopControlStatement s) {
		if (s.condition != null) {
			Expression condition = s.condition;
			if (isTruthy(evaluate(condition))) {
				if (s.isBreak) throw new Break();
				else throw new Continue();
			}
		}
		else {
			if (s.isBreak) throw new Break();
			else throw new Continue();
		}
	}
	
	public static void run(EnvisionInterpreter in, LoopControlStatement s) {
		new IS_LoopControl(in).run(s);
	}
	
}