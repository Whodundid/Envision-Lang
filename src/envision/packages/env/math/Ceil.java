package envision.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class Ceil extends EnvisionFunction {
	
	public Ceil() {
		super(Primitives.INT, "ceil", Primitives.NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionNumber num = (EnvisionNumber) args[0];
		double ceil = Math.ceil(num.doubleVal_i());
		EnvisionInt r = EnvisionIntClass.newInt(ceil);
		ret(r);
	}
	
}