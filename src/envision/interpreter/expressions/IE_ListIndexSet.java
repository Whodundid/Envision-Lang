package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionList;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_ListIndex;
import envision.parser.expressions.expression_types.Expr_SetListIndex;

public class IE_ListIndexSet extends ExpressionExecutor<Expr_SetListIndex> {

	public IE_ListIndexSet(EnvisionInterpreter in) {
		super(in);
	}
	
	public static Object run(EnvisionInterpreter in, Expr_SetListIndex e) {
		return new IE_ListIndexSet(in).run(e);
	}

	@Override
	public Object run(Expr_SetListIndex e) {
		Expr_ListIndex listIndexExpression = e.list;
		Expression value = e.value;
		
		Expression listExpression = listIndexExpression.list;
		Expression listExpressionIndex = listIndexExpression.index;
		Object listObject = evaluate(listExpression);
		Object listIndex = evaluate(listExpressionIndex);
		
		//assign the 'value' at the given 'listIndex'
		if (listObject instanceof EnvisionList env_list) {
			//default index
			long i = -1;
			
			if (listIndex instanceof Long long_val) 				i = long_val;
			else if (listIndex instanceof EnvisionInt env_int) 		i = env_int.long_val;
			
			Object theValue = evaluate(value);
			EnvisionObject env_obj = ObjectCreator.wrap(theValue);
			
			env_list.set(i, env_obj);
		}
		
		return null;
	}
	
}
