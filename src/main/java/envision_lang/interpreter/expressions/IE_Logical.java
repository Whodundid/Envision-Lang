package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.OperatorOverloadHandler;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.language_errors.error_types.InvalidDatatypeError;
import envision_lang.parser.expressions.expression_types.Expr_Logic;
import envision_lang.tokenizer.Operator;

public class IE_Logical extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Logic expression) {
		EnvisionObject a = interpreter.evaluate(expression.left);
		EnvisionObject b = interpreter.evaluate(expression.right);
		Operator op = expression.operator;
		
		// if a is a class instance, attempt to evaluate operator overloads
		if (a instanceof ClassInstance a_inst && a_inst.supportsOperator(op))
			return OperatorOverloadHandler.handleOverload(interpreter, null, op, a_inst, b);
		
		throw new InvalidDatatypeError("The given objects '" + a + "' and '" + b + "' cannot be logically evaluated!");
	}
	
}