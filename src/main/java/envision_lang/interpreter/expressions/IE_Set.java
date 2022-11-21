package envision_lang.interpreter.expressions;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.exceptions.errors.InvalidTargetError;
import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.parser.expressions.expression_types.Expr_Set;

public class IE_Set extends ExpressionExecutor<Expr_Set> {

	public IE_Set(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_Set e) {
		return new IE_Set(in).run(e);
	}
	
	@Override
	public EnvisionObject run(Expr_Set expression) {
		String name = expression.name.lexeme;
		EnvisionObject baseObject = evaluate(expression.object);
		EnvisionObject value = evaluate(expression.value);
		
		//don't allow java null to continue!
		//java null indicates something fundamentally went wrong inside of the interpreter
		if (value == null) {
			throw new NullVariableError("The result of '" + expression.value + "' produced Java:null!");
		}
		
		//copy if primitive value
		if (value.isPrimitive()) value = value.copy();
		
		//check for acceptable target types
		if (baseObject instanceof ClassInstance inst) return setValue_CI(inst, name, value);
		if (baseObject instanceof EnvisionCodeFile cf) return setValue_CF(cf, name, value);
		
		//error on non-accepted types
		throw new InvalidTargetError(baseObject + " cannot have values on it modified!");
	}
	
	private EnvisionObject setValue_CI(ClassInstance inst, String name, EnvisionObject value) {
		//check if the object is actually visible
		//if (object.isPrivate()) {
			//if the current scope is not the class instance's scope, throw an error
		//	if (scope() != inst.getScope()) throw new NotVisibleError(object);
		//}
		
		//usually there would be some type checking going on here -- but not yet..
		
		inst.set(name, value);
		return value;
	}
	
	private EnvisionObject setValue_CF(EnvisionCodeFile cf, String name, EnvisionObject value) {
		cf.scope().set(name, value);
		return value;
	}
	
//	private EnvisionObject setValue_ENUM(EnvisionEnum env_enum, String name, EnvisionObject value) {
//		
//	}
	
}
