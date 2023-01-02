package envision_lang.interpreter.util.creationUtil;

import envision_lang.exceptions.errors.NullVariableError;
import envision_lang.exceptions.errors.objects.UnsupportedOverloadError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.util.EnvisionParameter;
import envision_lang.lang.util.ParameterData;
import envision_lang.tokenizer.Operator;

public class OperatorOverloadHandler {
	
	/**
	 * Performs top-level class instance operator overload handles.
	 * If the base class instance natively supports the given operator overload,
	 * then attempt to directly run the given operator overload function.
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
		if (a.isPrimitive() || a instanceof EnvisionList) {
			//natively support right-handed string concatenations
			if (op == Operator.ADD && obj instanceof EnvisionString) {
				return EnvisionStringClass.concatenate(interpreter, a, obj);
			}
			//otherwise, allow the primitive class to try and find a valid Operator:Object handle
			else {
				return a.handleOperatorOverloads(interpreter, a_scopeName, op, obj);
			}
		}
		//otherwise, attempt to find a user-defined operator overload function on a user-defined class
		//if the defined object directly supports the given operator, grab the operator function and execute it
		else if (a.supportsOperator(op)) {
			EnvisionFunction op_func = getOperatorFunc(a, op, obj);
			//if the operator function is null -- skip this and jump to throwing error
			if (op_func != null) return op_func.invoke_r(interpreter, obj);
		}
		//natively support '=='
		else if (op == Operator.EQUALS) return interpreter.isEqual(a, obj);
		//natively support '!='
		else if (op == Operator.NOT_EQUALS) return interpreter.isEqual(a, obj).negate();
		
		//otherwise, throw error
		String errorMsg = (obj != null) ? obj.getDatatype() + ":" + obj : "";
		throw new UnsupportedOverloadError(a, op, errorMsg);
	}
	
	//-----------------
	// Private Methods
	//-----------------
	
	private static EnvisionFunction getOperatorFunc(ClassInstance c, Operator op, EnvisionObject b) {
		// first check if the class even has support for the given operator
		EnvisionFunction op_func = c.getOperator(op);
		if (op_func == null) return null;
		
		ParameterData params = new ParameterData();
		if (b != null) {
			// create parameters around the given 'b' target arg
			params.add(new EnvisionParameter(b));
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
