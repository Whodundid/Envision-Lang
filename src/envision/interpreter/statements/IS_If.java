package envision.interpreter.statements;

import envision.exceptions.errors.ExpressionError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.expressions.Expression;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_If;

/**
 * Evaluates if statements.
 * 
 * Every if statement has exactly one, non-null condition.
 * In the event that the given condition is null, a null error is thrown.
 * 
 * Each if statement has exactly one 'then' branch and an 'else' branch.
 * Both the then and else branches can potentially be completely null.
 * 
 * @author Hunter
 */
public class IS_If extends StatementExecutor<Stmt_If> {

	public IS_If(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_If s) {
		new IS_If(in).run(s);
	}

	@Override
	public void run(Stmt_If statement) {
		Expression cond = statement.condition;
		Statement thenBranch = statement.thenBranch;
		Statement elseBranch = statement.elseBranch;
		
		//if the given condition is null -- throw error
		if (cond == null) throw new ExpressionError("The given if condition is null!");
		
		//check if true
		if (isTrue(evaluate(cond))) execute(thenBranch);
		//otherwise if false
		else if (elseBranch != null) execute(elseBranch);
	}
	
}
