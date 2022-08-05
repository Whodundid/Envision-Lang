package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Super;

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
