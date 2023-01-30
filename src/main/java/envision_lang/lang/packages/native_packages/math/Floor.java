package envision_lang.lang.packages.native_packages.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.StaticTypes;

public class Floor extends EnvisionFunction {
	
	public Floor() {
		super(StaticTypes.INT_TYPE, "floor", StaticTypes.NUMBER_TYPE);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionNumber num = (EnvisionNumber) args[0];
		double floor = Math.floor(num.doubleVal_i());
		EnvisionInt r = EnvisionIntClass.valueOf((long) floor);
		ret(r);
	}
	
}
