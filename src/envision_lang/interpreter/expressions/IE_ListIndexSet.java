package envision_lang.interpreter.expressions;

import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.exceptions.errors.listErrors.NotAListError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.util.StaticTypes;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.expression_types.Expr_ListIndex;
import envision_lang.parser.expressions.expression_types.Expr_SetListIndex;

public class IE_ListIndexSet extends ExpressionExecutor<Expr_SetListIndex> {

	public IE_ListIndexSet(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_SetListIndex e) {
		return new IE_ListIndexSet(in).run(e);
	}

	@Override
	public EnvisionObject run(Expr_SetListIndex e) {
		Expr_ListIndex listIndexExpression = e.list;
		Expression value = e.value;
		
		Expression listExpression = listIndexExpression.list;
		Expression listExpressionIndex = listIndexExpression.index;
		EnvisionObject listObject = evaluate(listExpression);
		EnvisionObject listIndex = evaluate(listExpressionIndex);
		
		//only allow lists
		if (!(listObject instanceof EnvisionList)) throw new NotAListError(listObject);
		
		//assign the 'value' at the given 'listIndex'
		EnvisionList env_list = (EnvisionList) listObject;
		
		//default index
		long i = -1;
		
		//only allow integers for array indexes
		if (!(listIndex instanceof EnvisionInt))
			throw new InvalidDatatypeError(StaticTypes.INT_TYPE, listIndex.getDatatype());
		
		//get index as long
		i = ((EnvisionInt) listIndex).int_val;
		
		//assign the result to the index position
		EnvisionObject assign_value = evaluate(value);
		env_list.set(i, assign_value);
		
		//return the new value
		return assign_value;
	}
	
}
