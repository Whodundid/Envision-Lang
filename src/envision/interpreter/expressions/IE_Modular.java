package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expressions.ModularExpression;

public class IE_Modular extends ExpressionExecutor<ModularExpression> {

	public IE_Modular(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(ModularExpression expression) {
		System.out.println("returning: " + expression.modulars);
		return expression.modulars;
	}
	
	public static Object run(EnvisionInterpreter in, ModularExpression e) {
		return new IE_Modular(in).run(e);
	}
	
}
