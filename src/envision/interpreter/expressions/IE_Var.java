package envision.interpreter.expressions;

import envision.exceptions.errors.ExpressionError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.expression_types.Expr_Var;
import envision.tokenizer.Token;

/**
 * Handles individual variable lookup expressions.
 * <p>
 * In the event that the variable to be searched for is undefined
 * within the given scope, an UndefinedVariable error is thrown.
 * 
 * @author Hunter Bragg
 */
public class IE_Var extends ExpressionExecutor<Expr_Var> {

	//---------------------------------------------------------------------
	
	public IE_Var(EnvisionInterpreter in) {
		super(in);
	}

	public static EnvisionObject run(EnvisionInterpreter in, Expr_Var e) {
		return new IE_Var(in).run(e);
	}
	
	//---------------------------------------------------------------------
	
	@Override
	public EnvisionObject run(Expr_Var e) {
		Token name = e.name;
		if (name == null) throw new ExpressionError("The given name token for the expression '" + e + "' is null!");
		
		//get name from interpreter
		EnvisionObject obj = interpreter.lookUpVariable(e.name.lexeme);
		
		return obj;
	}
	
}
