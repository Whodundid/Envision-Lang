package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expressions.SuperExpression;

public class IE_Super extends ExpressionExecutor<SuperExpression> {

	public IE_Super(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(SuperExpression e) {
		//String target = e.target.lexeme;
		//EArrayList<Expression> args = e.args;
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, SuperExpression e) {
		return new IE_Super(in).run(e);
	}
	
}
