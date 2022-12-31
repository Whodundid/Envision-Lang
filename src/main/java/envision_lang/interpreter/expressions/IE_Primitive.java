package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.natives.Primitives;
import envision_lang.parser.expressions.expression_types.Expr_Primitive;

public class IE_Primitive extends ExpressionExecutor<Expr_Primitive> {

	public IE_Primitive(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Primitive e) {
		return new IE_Primitive(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Primitive expression) {
		Primitives type = Primitives.getPrimitiveType(expression.primitiveType);
		return NativeTypeManager.getClassTypeOf(type);
	}
	
}
