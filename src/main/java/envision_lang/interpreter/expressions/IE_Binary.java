package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.OperatorOverloadHandler;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.language_errors.error_types.ArithmeticError;
import envision_lang.parser.expressions.expression_types.Expr_Binary;
import envision_lang.tokenizer.KeywordType;
import envision_lang.tokenizer.Operator;

public class IE_Binary extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Binary expression) {
		EnvisionObject a = interpreter.evaluate(expression.left);
		EnvisionObject b = interpreter.evaluate(expression.right);
		
		// determine operation being made
		Operator op = expression.operator;
		
		// error if the operator is null
		if (op == null) throw new ArithmeticError("Null operator in arithmetic expression! '" + expression + "'!");
		
		// check if the operator is an assignment -- if so send to assign handler
		if (op.hasType(KeywordType.ASSIGNMENT)) return IE_Assign.handleAssign(interpreter, expression, op);
		
		// determine if the first object is a classInstance so that operator overloading can be handled
		if (a instanceof ClassInstance inst) {
			return OperatorOverloadHandler.handleOverload(interpreter, null, op, inst, b);
		}
		
        // otherwise, throw error
        throw new ArithmeticError("Cannot perform the operation '" + op + "' on the given object! '"
                                  + a + "' and '" + b + "'");
	}
}
