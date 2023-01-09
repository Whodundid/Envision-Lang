package envision_lang.interpreter.expressions;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.natives.IDatatype;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_TypeOf;

public class IE_TypeOf extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_TypeOf e) {
		ParsedExpression left = e.left;
		ParsedExpression right = e.right;
		boolean is = e.is;
		
		EnvisionObject lobj = interpreter.evaluate(left);
		EnvisionObject robj = interpreter.evaluate(right);
		
		IDatatype typeA = lobj.getDatatype();
		IDatatype typeB = robj.getDatatype();
		
		boolean same = typeA.compare(typeB);
		boolean val = (is) ? same : !same;
		
		return EnvisionBooleanClass.newBoolean(val);
	}
	
}
