package envision.packages.env.math;

import static envision.lang.util.Primitives.*;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionDoubleClass;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.internal.EnvisionFunction;

public class Log extends EnvisionFunction {
	
	public Log() {
		super(DOUBLE, "log", NUMBER);
		//support log(a / b)
		addOverload(DOUBLE, NUMBER, NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length == 1) {
			double val = ((EnvisionNumber) args[0]).doubleVal_i();
			ret(EnvisionDoubleClass.newDouble(Math.log10(val)));
		}
		else if (args.length == 2) {
			double a = ((EnvisionNumber) args[0]).doubleVal_i();
			double b = ((EnvisionNumber) args[1]).doubleVal_i();
			ret(EnvisionDoubleClass.newDouble(Math.log10(a / b)));
		}
	}
	
}
