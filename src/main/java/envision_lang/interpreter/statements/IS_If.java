package envision_lang.interpreter.statements;

import envision_lang.exceptions.errors.ExpressionError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.Statement;
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
		if (isTrue(evaluate(cond)))
			if (thenBranch != null) execute(thenBranch);
		//otherwise if false
		else if (elseBranch != null) execute(elseBranch);
	}
	
}
