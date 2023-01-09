package envision_lang.interpreter.expressions;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.NotVisibleError;
import envision_lang.exceptions.errors.RestrictedAccessError;
import envision_lang.exceptions.errors.UndefinedValueError;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.parser.expressions.expression_types.Expr_Get;

public class IE_Get extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Get expression) {
		EnvisionObject o = interpreter.evaluate(expression.object);
		//System.out.println("GET '" + expression + "' => " + o);
		
		if (o instanceof EnvisionClass clz) return getClassVal(interpreter, expression, clz);
		if (o instanceof ClassInstance inst) return getInstanceVal(interpreter, expression, inst);
		if (o instanceof EnvisionCodeFile code_file) return getImportVal(interpreter, expression, code_file);
		//if (o instanceof EnvisionEnum enum_obj) return getEnumValue(enum_obj);
		
		throw new EnvisionLangError("TEMP: Invalid get expression! " + expression + " : " + o + " : " + o.getClass());
	}
	
	//--------------------------------------------------------------------
	
	private static EnvisionObject getClassVal(EnvisionInterpreter interpreter, Expr_Get expression, EnvisionClass in) {
		EnvisionObject object = in.get(expression.name.getLexeme());
		
		//first check if the object even exists
		if (object == null) throw new UndefinedValueError(expression.name.getLexeme());
		
		//check if restricted
		if (object.isRestricted()) {
			throw new RestrictedAccessError(object);
		}
		
		//check if the object is actually visible
		if (object.isPrivate()) {
			//if the current scope is not the same as the instance's scope, throw an error
			if (interpreter.scope() != in.getClassScope()) throw new NotVisibleError(object);
		}
		
		return object;
	}
	
	private static EnvisionObject getInstanceVal(EnvisionInterpreter interpreter, Expr_Get expression, ClassInstance in) {
		EnvisionObject object = in.get(expression.name.getLexeme());
		
		//first check if the object even exists
		if (object == null) throw new UndefinedValueError(expression.name.getLexeme());
		
		//check if restricted
		if (object.isRestricted()) {
			throw new RestrictedAccessError(object);
		}
		
		//check if the object is actually visible
		if (object.isPrivate()) {
			//if the current scope is not the same as the instance's scope, throw an error
			if (interpreter.scope() != in.getScope()) throw new NotVisibleError(object);
		}
		
		return object;
	}
	
	private static EnvisionObject getImportVal(EnvisionInterpreter interpreter, Expr_Get expression, EnvisionCodeFile in) {
		EnvisionObject object = in.getValue(expression.name.getLexeme());
		
		//first check if the object even exists
		if (object == null) throw new UndefinedValueError(expression.name.getLexeme());
		
		//check if restricted
		if (object.isRestricted()) {
			throw new RestrictedAccessError(object);
		}
		
		//check if the object is actually visible
		//if (!object.isPublic()) {
			//if the current scope is not the same as the instance's scope, throw an error
			//throw new NotVisibleError(expression.name.getLexeme());
		//}
		
		return object;
	}
	
	/*
	private Object getEnumValue(EnvisionEnum in) {
		EnvisionObject object = in.getValue(expression.name.getLexeme());
		
		//first check if the object even exists
		if (object == null) throw new UndefinedValueError(expression.name.getLexeme());
		
		//if (object instanceof EnumValue) {
		//	return object;
		//}
		
		return null;
	}
	*/
	
}
