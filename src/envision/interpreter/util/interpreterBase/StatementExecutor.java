package envision.interpreter.util.interpreterBase;

import envision.interpreter.EnvisionInterpreter;
import envision.parser.statements.Statement;

public abstract class StatementExecutor<E extends Statement> extends InterpreterExecutor {
	
	protected StatementExecutor(EnvisionInterpreter in) { super(in); }
	public abstract void run(E statement);
	
}
