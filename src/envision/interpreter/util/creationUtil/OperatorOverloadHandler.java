package envision.interpreter.util.creationUtil;

import envision.exceptions.errors.objects.UnsupportedOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.internal.EnvisionFunction;
import envision.lang.internal.EnvisionVoid;
import envision.lang.util.Parameter;
import envision.lang.util.ParameterData;
import envision.lang.util.Primitives;
import envision.tokenizer.IKeyword;

public class OperatorOverloadHandler {
	
	public static Object handleOverload(EnvisionInterpreter interpreter, IKeyword op, ClassInstance a, Object b) {
		EnvisionFunction theOverload = getOperatorMethod(a, op, b);
		
		// if the overload exists, run the operator overload method
		if (theOverload != null) {
			try {
				theOverload.invoke(interpreter, ObjectCreator.wrap(b));
			}
			catch (ReturnValue r) {
				return r.object;
			}
			//return void if nothing was returned from the overload
			return EnvisionVoid.VOID;
		}
		
		// otherwise, check to see what operator is being evaluated as some may still apply regardless
		// default operators for every object
		if (op.isOperator()) {
			var operator = op.asOperator();
			
			//check for string additoins
			//if (operator == Operator.ADD) {
				//only allow strings
				//if (b instanceof String str) {
					//convert the object to it's string form
					//String obj_str = getToString(interpreter, a);
					//return EnvisionStringClass.newString(obj_str + str);
				//}
				
				//throw new UnsupportedOverloadError(a, op, Primitives.getDataType(b) + ":" + b);
			//}
			
			//otherwise check for any additional valid default operators
			switch (operator) {
			//case ASSIGN: return IE_Assign.assign(interpreter, a.getName(), a, b);
			case COMPARE: return interpreter.isEqual(a, b);
			case NOT_EQUALS: return !interpreter.isEqual(a, b);
			default: break;
			}
		}
		
		throw new UnsupportedOverloadError(a, op, Primitives.getDataType(b) + ":" + b);
	}
	
	//-----------------
	// Private Methods
	//-----------------
	
	private static EnvisionFunction getOperatorMethod(ClassInstance c, IKeyword op, Object b) {
		// first check if the class even has support for the given operator
		EnvisionFunction op_func = c.getOperator(op);
		if (op_func == null) return null;
		
		ParameterData params = new ParameterData();
		if (b != null) {
			// create parameters around the given 'b' target arg
			EnvisionObject obj = ObjectCreator.wrap(b);
			params.add(new Parameter(obj));
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
