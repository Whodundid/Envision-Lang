package envision_lang.interpreter.expressions;

import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Ternary;

public class IE_Ternary extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Ternary expression) {
		ParsedExpression condition = expression.condition;
		ParsedExpression ifTrue = expression.ifTrue;
		ParsedExpression ifFalse = expression.ifFalse;
		
		EnvisionObject value = interpreter.evaluate(condition);
		
		//check that the value is actually a boolean
		if (!(value instanceof EnvisionBoolean)) {
			throw new InvalidDatatypeError("Expected a boolean here but got '" + value + "' instead!");
		}
		
		return (isTrue(value)) ? interpreter.evaluate(ifTrue) : interpreter.evaluate(ifFalse);
	}
	
}