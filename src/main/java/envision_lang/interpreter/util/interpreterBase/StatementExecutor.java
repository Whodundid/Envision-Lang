package envision_lang.interpreter.util.interpreterBase;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.parser.statements.Statement;

public abstract class StatementExecutor<E extends Statement> extends InterpreterExecutor {
	
	protected StatementExecutor(EnvisionInterpreter in) { super(in); }
	public abstract void run(E statement);
	
}
