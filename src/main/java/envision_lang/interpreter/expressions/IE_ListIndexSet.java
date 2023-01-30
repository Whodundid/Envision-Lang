package envision_lang.interpreter.expressions;

import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.exceptions.errors.listErrors.NotAListError;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.OperatorOverloadHandler;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_ListIndex;
import envision_lang.parser.expressions.expression_types.Expr_SetListIndex;
import envision_lang.tokenizer.Operator;

public class IE_ListIndexSet extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_SetListIndex e) {
		Expr_ListIndex listIndexExpression = e.list;
		Operator operator = e.operator;
		ParsedExpression value = e.value;
		
		ParsedExpression listExpression = listIndexExpression.list;
		ParsedExpression listExpressionIndex = listIndexExpression.index;
		
		EnvisionObject listObject = interpreter.evaluate(listExpression);
		EnvisionObject listIndex = interpreter.evaluate(listExpressionIndex);
		
		// only allow lists
		if (!(listObject instanceof EnvisionList)) throw new NotAListError(listObject);
		
		// assign the 'value' at the given 'listIndex'
		EnvisionList env_list = (EnvisionList) listObject;
		
		// default index
		long i = -1;
		
		// only allow integers for array indexes
		if (!(listIndex instanceof EnvisionInt)) {
			throw new InvalidDatatypeError(StaticTypes.INT_TYPE, listIndex.getDatatype());
		}
		
		// get index as long
		i = ((EnvisionInt) listIndex).int_val;
		
		// assign the result to the index position
		EnvisionObject assign_value = interpreter.evaluate(value);
		EnvisionObject existingObject = env_list.get(i);
		
		// check if class instance and that it supports the given operator overload
		if (existingObject instanceof ClassInstance env_class) {
			if (env_class.supportsOperator(operator)) {
				return OperatorOverloadHandler.handleOverload(interpreter, null, operator, env_class, assign_value);
			}
		}
		
		// do normal assign
		if (assign_value.isPassByValue()) {
			env_list.set(i, assign_value).copy();
		}
		else {
			env_list.set(i, assign_value);
		}
		
		// return the new value
		return assign_value;
	}
	
}
