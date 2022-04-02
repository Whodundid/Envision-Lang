package envision.interpreter.util.interpreterBase;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;

public abstract class ExpressionExecutor<E extends Expression> extends InterpreterExecutor {

	protected ExpressionExecutor(EnvisionInterpreter in) { super(in); }
	public abstract EnvisionObject run(E e);
	
}
