package envision.interpreter.util.creationUtil;

import envision.exceptions.errors.NullVariableError;
import envision.exceptions.errors.objects.UnsupportedOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Parameter;
import envision.lang.util.ParameterData;
import envision.tokenizer.Operator;

public class OperatorOverloadHandler {
	
	/**
	 * Performs top-level class instance operator overload handles.
	 * If the base class instance natively supports the given operator overload,
	 * then attempt to directely run the given operator overload function.
	 * 
	 * @param interpreter
	 * @param a_scopeName
	 * @param op
	 * @param a
	 * @param obj
	 * @return
	 */
	public static EnvisionObject handleOverload(EnvisionInterpreter interpreter,
												String a_scopeName,
											    Operator op,
											    ClassInstance a,
											    EnvisionObject obj)
	{
		//error on null base objects
		if (a == null) throw new NullVariableError();
		
		//if object is a primitive, handle native primitive overloads
		if (a.isPrimitive()) return a.handleOperatorOverloads(interpreter, a_scopeName, op, obj);
		
		//if the base object directly supports the operator, grab the operator function and execute it
		if (a.supportsOperator(op)) {
			EnvisionFunction op_func = getOperatorMethod(a, op, obj);
			//if the operator function is null -- skip this and jump to throwing error
			if (op_func != null) return op_func.invoke_r(interpreter, obj);
		}
		
		//otherwise, throw error
		String errorMsg = (obj != null) ? obj.getDatatype() + ":" + obj : "";
		throw new UnsupportedOverloadError(a, op, errorMsg);
	}
	
	//-----------------
	// Private Methods
	//-----------------
	
	private static EnvisionFunction getOperatorMethod(ClassInstance c, Operator op, EnvisionObject b) {
		// first check if the class even has support for the given operator
		EnvisionFunction op_func = c.getOperator(op);
		if (op_func == null) return null;
		
		ParameterData params = new ParameterData();
		if (b != null) {
			// create parameters around the given 'b' target arg
			params.add(new Parameter(b));
		}
		
		EnvisionFunction theOverload = null;
		
		// check if the overload supports the given target parameter
		if (op_func.getParams().compare(params)) {
			theOverload = op_func;
		}
		// otherwise check if any of the overload's overloads support the parameter
		else {
			EnvisionFunction funcOverload = op_func.getOverload(params);
			if (funcOverload != null) theOverload = funcOverload;
		}
		
		//return the overload, even if null
		return theOverload;
	}
	
}
