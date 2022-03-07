package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.parser.expressions.expression_types.VarExpression;

/**
 * Handles individual variable lookup expressions.
 * <p>
 * In the event that the variable to be searched for is undefined
 * within the given scope, an UndefinedVariable error is thrown.
 * 
 * @author Hunter Bragg
 */
public class IE_Var extends ExpressionExecutor<VarExpression> {

	//---------------------------------------------------------------------
	
	public IE_Var(EnvisionInterpreter in) {
		super(in);
	}

	public static Object run(EnvisionInterpreter in, VarExpression e) {
		return new IE_Var(in).run(e);
	}
	
	//---------------------------------------------------------------------
	
	@Override
	public Object run(VarExpression expression) {
		return interpreter.lookUpVariable(expression.name);
	}
	
}
