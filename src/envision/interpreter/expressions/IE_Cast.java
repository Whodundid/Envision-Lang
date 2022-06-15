package envision.interpreter.expressions;

import envision.exceptions.errors.UndefinedTypeError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.TypeManager;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.natives.IDatatype;
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
