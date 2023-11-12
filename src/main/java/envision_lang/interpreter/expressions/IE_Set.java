package envision_lang.interpreter.expressions;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.language_errors.error_types.InvalidTargetError;
import envision_lang.lang.language_errors.error_types.NotVisibleError;
import envision_lang.lang.language_errors.error_types.NullVariableError;
import envision_lang.lang.language_errors.error_types.UndefinedValueError;
import envision_lang.parser.expressions.expression_types.Expr_Set;

public class IE_Set extends AbstractInterpreterExecutor {
	
	public static EnvisionObject run(EnvisionInterpreter interpreter, Expr_Set expression) {
		String name = expression.name.getLexeme();
		EnvisionObject baseObject = interpreter.evaluate(expression.object);
		EnvisionObject value = interpreter.evaluate(expression.value);
		
		// don't allow java null to continue!
		// java null indicates something fundamentally went wrong inside of the interpreter
		if (value == null) {
			throw new NullVariableError("The result of '" + expression.value + "' produced Java:null!");
		}
		
		// copy if primitive value
		if (value.isPassByValue()) value = value.copy();
		
		// check for acceptable target types
		if (baseObject instanceof EnvisionClass c) return setValue_C(interpreter, c, name, value);
		if (baseObject instanceof ClassInstance inst) return setValue_CI(interpreter, inst, name, value);
		if (baseObject instanceof EnvisionCodeFile cf) return setValue_CF(interpreter, cf, name, value);
		
		// error on non-accepted types
		throw new InvalidTargetError(baseObject + " cannot have values on it modified!");
	}
	
	private static EnvisionObject setValue_C(EnvisionInterpreter interpreter, EnvisionClass theClass,
                                              String name, EnvisionObject value)
	{
	       //check if the object is actually visible
        if (theClass.isPrivate()) {
            // if the current scope is not the class the Class's scope, throw an error
            if (interpreter.scope() != theClass.getClassScope()) throw new NotVisibleError(theClass);
        }
        
        // TODO:
        // usually there would be some type checking going on here -- but not yet..
        
        var scope = theClass.getClassScope();
        boolean exists = scope.exists(name);
        
        // if the value exists, attempt to set the value to the existing space
        if (exists) {
            theClass.set(name, value);
        }
        // otherwise, define it as a var on the object directly
        else {
            //scope.define(name, EnvisionStaticTypes.VAR_TYPE, value);
            throw new UndefinedValueError(name);
        }
        
        return value;
	}
	
	private static EnvisionObject setValue_CI(EnvisionInterpreter interpreter, ClassInstance inst,
	                                          String name, EnvisionObject value)
	{
		//check if the object is actually visible
		if (inst.isPrivate()) {
			// if the current scope is not the class instance's scope, throw an error
			if (interpreter.scope() != inst.getScope()) throw new NotVisibleError(inst);
		}
		
		// TODO:
		// usually there would be some type checking going on here -- but not yet..
		
	    var scope = inst.getScope();
	    boolean exists = scope.exists(name);
	    
	    // if the value exists, attempt to set the value to the existing space
	    if (exists) {
	        inst.set(name, value);
	    }
	    // otherwise, define it as a var on the object directly
	    else {
	        //scope.define(name, EnvisionStaticTypes.VAR_TYPE, value);
	        throw new UndefinedValueError(name);
	    }
		
		return value;
	}
	
	private static EnvisionObject setValue_CF(EnvisionInterpreter interpreter, EnvisionCodeFile cf,
	                                          String name, EnvisionObject value)
	{
		cf.scope().set(name, value);
		return value;
	}
	
//	private EnvisionObject setValue_ENUM(EnvisionEnum env_enum, String name, EnvisionObject value) {
//		
//	}
	
}
