package envision.interpreter.expressions;

import envision.EnvisionCodeFile;
import envision.exceptions.EnvisionError;
import envision.exceptions.errors.NotVisibleError;
import envision.exceptions.errors.RestrictedAccessError;
import envision.exceptions.errors.UndefinedValueError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.enums.EnumValue;
import envision.lang.enums.EnvisionEnum;
import envision.lang.util.structureTypes.InheritableObject;
import envision.parser.expressions.expression_types.Expr_Get;

public class IE_Get extends ExpressionExecutor<Expr_Get> {

	private Expr_Get expression;
	
	//--------------------------------------------------------------------
	
	public IE_Get(EnvisionInterpreter in) {
		super(in);
	}
	
	//--------------------------------------------------------------------

	@Override
	public Object run(Expr_Get e) {
		Object o = evaluate((expression = e).object);
		
		if (o instanceof ClassInstance) { return getInstanceVal((ClassInstance) o); }
		if (o instanceof InheritableObject) { }
		if (o instanceof EnvisionCodeFile) { return getImportVal((EnvisionCodeFile) o); }
		if (o instanceof EnvisionEnum) { return getEnumValue((EnvisionEnum) o); }
		
		throw new EnvisionError("TEMP: Invalid get expression! " + e + " : " + o);
	}
	
	//--------------------------------------------------------------------
	
	private Object getInstanceVal(ClassInstance in) {
		EnvisionObject object = in.get(expression.name.lexeme);
		
		//first check if the object even exists
		if (object == null) { throw new UndefinedValueError(expression.name.lexeme); }
		
		//check if restricted
		if (object.isRestricted()) {
			throw new RestrictedAccessError(object);
		}
		
		//check if the object is actually visible
		if (object.isPrivate() || object.isProtected()) {
			//if the current scope is not the same as the instance's scope, throw an error
			if (scope() != in.getScope()) { throw new NotVisibleError(object); }
		}
		
		return object;
	}
	
	private Object getImportVal(EnvisionCodeFile in) {
		EnvisionObject object = in.getValue(expression.name.lexeme);
		
		//first check if the object even exists
		if (object == null) { throw new UndefinedValueError(expression.name.lexeme); }
		
		//check if restricted
		if (object.isRestricted()) {
			throw new RestrictedAccessError(object);
		}
		
		//check if the object is actually visible
		if (!object.isPublic()) {
			//if the current scope is not the same as the instance's scope, throw an error
			throw new NotVisibleError(object);
		}
		
		return object;
	}
	
	private Object getEnumValue(EnvisionEnum in) {
		EnvisionObject object = in.getValue(expression.name.lexeme);
		
		//first check if the object even exists
		if (object == null) { throw new UndefinedValueError(expression.name.lexeme); }
		
		if (object instanceof EnumValue) {
			return object;
		}
		
		return null;
	}
	
	//--------------------------------------------------------------------
	
	public static Object run(EnvisionInterpreter in, Expr_Get e) {
		return new IE_Get(in).run(e);
	}
	
}
