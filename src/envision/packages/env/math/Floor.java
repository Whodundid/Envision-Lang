package envision.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class Floor extends EnvisionFunction {
	
	public Floor() {
		super(Primitives.INT, "floor", Primitives.NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionNumber num = (EnvisionNumber) args[0];
		double floor = Math.floor(num.doubleVal_i());
		EnvisionInt r = EnvisionIntClass.newInt(floor);
		ret(r);
	}
	
}
