package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.TypeOfExpression;

public class IE_TypeOf extends ExpressionExecutor<TypeOfExpression> {

	public IE_TypeOf(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(TypeOfExpression e) {
		Expression left = e.left;
		Expression right = e.right;
		boolean is = e.is;
		
		EnvisionObject lobj = ObjectCreator.wrap(evaluate(left));
		EnvisionObject robj = ObjectCreator.wrap(evaluate(right));
		
		boolean same = lobj.equals(robj);
		
		return (is) ? same : !same;
	}
	
	public static Object run(EnvisionInterpreter in, TypeOfExpression e) {
		return new IE_TypeOf(in).run(e);
	}
	
}
