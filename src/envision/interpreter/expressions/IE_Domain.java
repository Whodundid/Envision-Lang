package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.expression_types.Expr_Domain;

public class IE_Domain extends ExpressionExecutor<Expr_Domain> {

	public IE_Domain(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Domain e) {
		return new IE_Domain(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Domain expression) {
		//not implemented
		return null;
	}
	
}
