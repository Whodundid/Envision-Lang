package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Modular;

public class IE_Modular extends ExpressionExecutor<Expr_Modular> {

	public IE_Modular(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Modular e) {
		return new IE_Modular(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Modular expression) {
		//System.out.println("returning: " + expression.modulars);
		//return expression.modulars;
		return null;
	}
	
}
