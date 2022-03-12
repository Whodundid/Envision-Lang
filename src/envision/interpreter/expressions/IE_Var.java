package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.Expr_Var;

/**
 * Handles individual variable lookup expressions.
 * <p>
 * In the event that the variable to be searched for is undefined
 * within the given scope, an UndefinedVariable error is thrown.
 * 
 * @author Hunter Bragg
 */
public class IE_Var extends ExpressionExecutor<Expr_Var> {

	//---------------------------------------------------------------------
	
	public IE_Var(EnvisionInterpreter in) {
		super(in);
	}

	public static Object run(EnvisionInterpreter in, Expr_Var e) {
		return new IE_Var(in).run(e);
	}
	
	//---------------------------------------------------------------------
	
	@Override
	public Object run(Expr_Var expression) {
		return interpreter.lookUpVariable(expression.name);
	}
	
}
