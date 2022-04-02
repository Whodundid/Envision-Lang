package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionBooleanClass;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_TypeOf;

public class IE_TypeOf extends ExpressionExecutor<Expr_TypeOf> {

	public IE_TypeOf(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_TypeOf e) {
		return new IE_TypeOf(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_TypeOf e) {
		Expression left = e.left;
		Expression right = e.right;
		boolean is = e.is;
		
		EnvisionObject lobj = evaluate(left);
		EnvisionObject robj = evaluate(right);
		
		boolean same = lobj.equals(robj);
		boolean val = (is) ? same : !same;
		
		return EnvisionBooleanClass.newBoolean(val);
	}
	
}
