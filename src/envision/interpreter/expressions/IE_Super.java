package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.expression_types.Expr_Super;

public class IE_Super extends ExpressionExecutor<Expr_Super> {

	public IE_Super(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Super e) {
		return new IE_Super(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Super e) {
		//String target = e.target.lexeme;
		//EArrayList<Expression> args = e.args;
		
		return null;
	}

}
