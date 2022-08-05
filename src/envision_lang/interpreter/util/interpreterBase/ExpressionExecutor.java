package envision_lang.interpreter.util.interpreterBase;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;

public abstract class ExpressionExecutor<E extends Expression> extends InterpreterExecutor {

	protected ExpressionExecutor(EnvisionInterpreter in) { super(in); }
	public abstract EnvisionObject run(E e);
	
}
