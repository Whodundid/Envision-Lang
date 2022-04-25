package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.TypeManager;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionNull;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_Cast;

public class IE_Cast extends ExpressionExecutor<Expr_Cast> {

	public IE_Cast(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Cast e) {
		return new IE_Cast(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Cast e) {
		String toType = e.toType.lexeme;
		Expression target = e.target;
		
		//grab typeClass
		TypeManager typeMan = interpreter.getTypeManager();
		//EnvisionObject typeMan.
		
		//placeholder
		return EnvisionNull.NULL;
	}
	
}
