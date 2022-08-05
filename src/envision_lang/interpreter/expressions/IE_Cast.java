package envision_lang.interpreter.expressions;

import envision_lang.exceptions.errors.UndefinedTypeError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.TypeManager;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.natives.IDatatype;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.expression_types.Expr_Cast;

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
		Expression target_expr = e.target;
		
		//grab typeClass
		TypeManager typeMan = interpreter.getTypeManager();
		EnvisionClass typeClass = typeMan.getTypeClass(toType);
		
		//System.out.println("CAST: " + typeClass);
		//System.out.println("TARGET: " + target_expr);
		
		//ensure the type to cast to actually exists
		if (typeClass == null) throw UndefinedTypeError.badType(toType);
		
		IDatatype cast_type = typeClass.getDatatype();
		EnvisionObject target_obj = evaluate(target_expr);
		
		//attempt to handle cast
		if (target_obj instanceof ClassInstance inst) {
			return inst.handleObjectCasts(cast_type);
		}
		else {
			throw new RuntimeException("No idea how to handle object cast atm!");
		}
	}
	
}
