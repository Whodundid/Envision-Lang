package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.natives.Primitives;
import envision_lang.parser.expressions.expression_types.Expr_Primitive;

public class IE_Primitive extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Primitive expression) {
		Primitives type = Primitives.getPrimitiveType(expression.primitiveType);
		
		EnvisionClass classType = NativeTypeManager.getClassTypeOf(type);
		if (classType != null) return classType;
		
		return NativeTypeManager.getClassTypeOf(type);
	}
	
}
