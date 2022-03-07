package envision.interpreter.expressions;

import envision.exceptions.errors.EnumReassignmentError;
import envision.exceptions.errors.FinalVarReassignmentError;
import envision.exceptions.errors.NotVisibleError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.enums.EnumValue;
import envision.lang.enums.EnvisionEnum;
import envision.parser.expressions.expression_types.SetExpression;

public class IE_Set extends ExpressionExecutor<SetExpression> {

	public IE_Set(EnvisionInterpreter in) {
		super(in);
	}

	@Override
	public Object run(SetExpression expression) {
		Object baseObject = evaluate(expression.object);
		
		// error if the object being set is an enum
		if (baseObject instanceof EnvisionEnum env_enum) throw new EnumReassignmentError(env_enum, expression.value);
		// error if the object being set is an enum value
		if (baseObject instanceof EnumValue) throw new FinalVarReassignmentError(baseObject);
		
		// check if the base object is a class
		if (baseObject instanceof ClassInstance inst) {
			EnvisionObject object = inst.get(expression.name.lexeme);
			
			//check if the object is actually visible
			if (object.isPrivate()) {
				//if the current scope is not the class instance's scope, throw an error
				if (scope() != inst.getScope()) throw new NotVisibleError(object);
			}
			
			//usually there would be some type checking going on here -- but not yet..
			
			Object value = evaluate(expression.value);
			if (value instanceof EnvisionObject env_obj) {
				inst.set(expression.name.lexeme, env_obj);
			}
			//try to make it into an object
			else {
				EnvisionObject o = ObjectCreator.wrap(value);
				if (o != null) {
					inst.set(expression.name.lexeme, o);
				}
			}
		}
		
		return null;
	}
	
	public static Object run(EnvisionInterpreter in, SetExpression e) {
		return new IE_Set(in).run(e);
	}
	
}
