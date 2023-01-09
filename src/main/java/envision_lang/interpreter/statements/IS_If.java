package envision_lang.interpreter.statements;

import envision_lang.exceptions.errors.ExpressionError;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_If;

/**
 * Evaluates if statements.
 * 
 * Every if statement has exactly one, non-null condition.
 * In the event that the given condition is null, a null error is thrown.
 * 
 * Each if statement has exactly one 'then' branch and an optional 'else' branch.
 * Both branches can potentially be completely null if not defined.
 * 
 * @author Hunter Bragg
 */
public class IS_If extends AbstractInterpreterExecutor {
	
	private IS_If() {}
	
	public static void run(EnvisionInterpreter interpreter, Stmt_If statement) {
		ParsedExpression cond = statement.condition;
		ParsedStatement thenBranch = statement.thenBranch;
		ParsedStatement elseBranch = statement.elseBranch;
		
		//if the given condition is null -- throw error
		if (cond == null) throw new ExpressionError("The given if condition is null!");
		
		//check if true
		if (isTrue(interpreter.evaluate(cond))) {
			if (thenBranch != null) {
				interpreter.execute(thenBranch);
			}
		}
		//otherwise if false
		else if (elseBranch != null) {
			interpreter.execute(elseBranch);
		}
	}
	
}
