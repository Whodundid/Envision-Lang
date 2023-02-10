package envision_lang.interpreter.expressions.unused;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.UserDefinedTypeManager;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.language_errors.error_types.UndefinedTypeError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.unused.Expr_Cast;

public class IE_Cast extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Cast e) {
		String toType = e.toType.getLexeme();
		ParsedExpression target_expr = e.target;
		
		// grab type class
		UserDefinedTypeManager typeMan = interpreter.getTypeManager();
		EnvisionClass typeClass = typeMan.getTypeClass(toType);
		
		System.out.println("CAST: " + typeClass);
		System.out.println("TARGET: " + target_expr);
		
		// ensure the type to cast to actually exists
		if (typeClass == null) throw UndefinedTypeError.badType(toType);
		
		IDatatype cast_type = typeClass.getDatatype();
		EnvisionObject target_obj = interpreter.evaluate(target_expr);
		
		// attempt to handle cast
		if (target_obj instanceof ClassInstance inst) {
			return inst.handleObjectCasts(cast_type);
		}
		else {
			throw new RuntimeException("No idea how to handle object cast atm!");
		}
	}
	
}
