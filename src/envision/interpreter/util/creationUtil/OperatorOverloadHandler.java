package envision.interpreter.util.creationUtil;

import envision.exceptions.errors.objects.UnsupportedOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.expressions.IE_Assign;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.objects.EnvisionMethod;
import envision.lang.objects.EnvisionVoidObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.data.Parameter;
import envision.lang.util.data.ParameterData;
import envision.tokenizer.Keyword;
import eutil.datatypes.EArrayList;

public class OperatorOverloadHandler {
	
	public static Object handleOverload(EnvisionInterpreter interpreter, Keyword op, ClassInstance a, Object b) {
		EnvisionMethod theOverload = getOperatorMethod(a, op, b);
		
		// if the overload exists, run the operator overload method
		if (theOverload != null) {
			try {
				theOverload.invoke(interpreter, b);
			}
			catch (ReturnValue r) {
				return r.object;
			}
			//return void if nothing was returned from the overload
			return new EnvisionVoidObject();
		}
		
		// otherwise, check to see what operator is being evaluated as some may still apply regardless
		// default operators for every object
		switch (op) {
		case ASSIGN: return IE_Assign.assign(interpreter, a.getName(), a, b);
		case COMPARE: return interpreter.isEqual(a, b);
		case NOT_EQUALS: return !interpreter.isEqual(a, b);
		default: throw new UnsupportedOverloadError(a, op, EnvisionDataType.getDataType(b) + ":" + b);
		}
	}
	
	//-----------------
	// Private Methods
	//-----------------
	
	private static EnvisionMethod getOperatorMethod(ClassInstance c, Keyword op, Object b) {
		// first check if the class even has support for the given operator
		EArrayList<EnvisionMethod> overloads = c.getOperator(op);
		if (overloads == null) return null;
		
		ParameterData params = new ParameterData();
		if (b != null) {
			// create parameters around the given 'b' target arg
			EnvisionObject obj = ObjectCreator.wrap(b);
			params.add(new Parameter(obj));
		}
		
		EnvisionMethod theOverload = null;
		for (EnvisionMethod m : overloads) {
			
			// check if the overload supports the given target parameter
			if (m.getParams().compare(params)) {
				theOverload = m;
				break;
			}
			// otherwise check if any of the overload's overloads support the parameter
			else {
				EnvisionMethod methOverload = m.getOverload(params);
				if (methOverload == null) continue;
				theOverload = methOverload;
				break;
			}
		}
		
		//return the overload, even if null
		return theOverload;
	}
	
}
