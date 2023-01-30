package envision_lang.packages.env.math;

import static envision_lang.lang.natives.Primitives.*;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;

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
			ret(EnvisionDoubleClass.valueOf(Math.log10(val)));
		}
		else if (args.length == 2) {
			double a = ((EnvisionNumber) args[0]).doubleVal_i();
			double b = ((EnvisionNumber) args[1]).doubleVal_i();
			ret(EnvisionDoubleClass.valueOf(Math.log10(a / b)));
		}
	}
	
}
