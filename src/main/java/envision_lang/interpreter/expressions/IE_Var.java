package envision_lang.interpreter.expressions;

import envision_lang.exceptions.errors.ExpressionError;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.tokenizer.Token;

/**
 * Handles individual variable lookup expressions.
 * <p>
 * In the event that the variable to be searched for is undefined
 * within the given scope, an UndefinedVariable error is thrown.
 * 
 * @author Hunter Bragg
 */
public class IE_Var extends AbstractInterpreterExecutor {

	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Var e) {
		Token name = e.name;
		if (name == null)
			throw new ExpressionError("The given name token for the expression '" + e + "' is null!");
		
		//get name from interpreter
		EnvisionObject obj = interpreter.lookUpVariable(e.name.getLexeme());
		
		return obj;
	}
	
}
