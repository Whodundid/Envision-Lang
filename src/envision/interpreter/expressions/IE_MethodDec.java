package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.types.MethodDeclarationExpression;

public class IE_MethodDec extends ExpressionExecutor<MethodDeclarationExpression> {

	public IE_MethodDec(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Void run(MethodDeclarationExpression expression) {
		interpreter.execute(expression.declaration);
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, MethodDeclarationExpression e) {
		return new IE_MethodDec(in).run(e);
	}
	
}
