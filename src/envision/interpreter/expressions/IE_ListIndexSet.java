package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.objects.EnvisionList;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.ListIndexExpression;
import envision.parser.expressions.expression_types.ListIndexSetExpression;

public class IE_ListIndexSet extends ExpressionExecutor<ListIndexSetExpression> {

	public IE_ListIndexSet(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(ListIndexSetExpression e) {
		ListIndexExpression listIndexExpression = e.list;
		Expression value = e.value;
		
		Expression listExpression = listIndexExpression.list;
		Expression listExpressionIndex = listIndexExpression.index;
		Object listObject = evaluate(listExpression);
		Object listIndex = evaluate(listExpressionIndex);
		
		if (listObject instanceof EnvisionList) {
			EnvisionList l = (EnvisionList) listObject;
			long i = -1;
			if (listIndex instanceof Long) { i = (Long) listIndex; }
			else if (listIndex instanceof EnvisionInt) { i = (long) ((EnvisionInt) listIndex).get(); }
			
			Object theValue = evaluate(value);
			Object obj = EnvisionObject.convert(theValue);
			
			l.set(i, obj);
		}
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, ListIndexSetExpression e) {
		return new IE_ListIndexSet(in).run(e);
	}
	
}
