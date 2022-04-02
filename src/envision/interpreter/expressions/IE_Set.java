package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.parser.expressions.expression_types.Expr_Set;

public class IE_Set extends ExpressionExecutor<Expr_Set> {

	public IE_Set(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Set e) {
		return new IE_Set(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Set expression) {
		EnvisionObject baseObject = evaluate(expression.object);
		
		// error if the object being set is an enum
		//if (baseObject instanceof EnvisionEnum env_enum) throw new EnumReassignmentError(env_enum, expression.value);
		// error if the object being set is an enum value
		//if (baseObject instanceof EnumValue) throw new FinalVarReassignmentError(baseObject);
		
		// check if the base object is a class
		if (baseObject instanceof ClassInstance inst) {
			//EnvisionObject object = inst.get(expression.name.lexeme);
			
			//check if the object is actually visible
			//if (object.isPrivate()) {
				//if the current scope is not the class instance's scope, throw an error
			//	if (scope() != inst.getScope()) throw new NotVisibleError(object);
			//}
			
			//usually there would be some type checking going on here -- but not yet..
			
			EnvisionObject value = evaluate(expression.value);
			inst.set(expression.name.lexeme, value);
		}
		
		return null;
	}
	
}
