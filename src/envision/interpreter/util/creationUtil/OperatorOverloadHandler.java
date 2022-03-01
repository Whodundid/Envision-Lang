package envision.interpreter.util.creationUtil;

import envision.exceptions.errors.objects.UnsupportedOverloadError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.expressions.IE_Assign;
import envision.interpreter.util.throwables.ReturnValue;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.objects.EnvisionFunction;
import envision.lang.objects.EnvisionVoidObject;
import envision.lang.util.Primitives;
import envision.lang.util.data.Parameter;
import envision.lang.util.data.ParameterData;
import envision.tokenizer.IKeyword;
import eutil.datatypes.EArrayList;

public class OperatorOverloadHandler {
	
	public static Object handleOverload(EnvisionInterpreter interpreter, IKeyword op, ClassInstance a, Object b) {
		EnvisionFunction theOverload = getOperatorMethod(a, op, b);
		
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
		if (op.isOperator()) {
			switch (op.asOperator()) {
			case ASSIGN: return IE_Assign.assign(interpreter, a.getName(), a, b);
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
		EArrayList<EnvisionFunction> overloads = c.getOperator(op);
		if (overloads == null) return null;
		
		ParameterData params = new ParameterData();
		if (b != null) {
			// create parameters around the given 'b' target arg
			EnvisionObject obj = ObjectCreator.wrap(b);
			params.add(new Parameter(obj));
		}
		
		EnvisionFunction theOverload = null;
		for (EnvisionFunction m : overloads) {
			
			// check if the overload supports the given target parameter
			if (m.getParams().compare(params)) {
				theOverload = m;
				break;
			}
			// otherwise check if any of the overload's overloads support the parameter
			else {
				EnvisionFunction methOverload = m.getOverload(params);
				if (methOverload == null) continue;
				theOverload = methOverload;
				break;
			}
		}
		
		//return the overload, even if null
		return theOverload;
	}
	
}
