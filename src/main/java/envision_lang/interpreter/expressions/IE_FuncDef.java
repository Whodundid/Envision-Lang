package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.expression_types.Expr_FuncDef;

public class IE_FuncDef extends ExpressionExecutor<Expr_FuncDef> {

	public IE_FuncDef(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_FuncDef e) {
		return new IE_FuncDef(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_FuncDef expression) {
		interpreter.execute(expression.declaration);
		return null;
	}
	
}
