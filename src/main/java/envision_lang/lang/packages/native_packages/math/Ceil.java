package envision_lang.lang.packages.native_packages.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class Ceil extends EnvisionFunction {
	
	public Ceil() {
		super(EnvisionStaticTypes.INT_TYPE, "ceil", EnvisionStaticTypes.NUMBER_TYPE);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionNumber num = (EnvisionNumber) args[0];
		double ceil = Math.ceil(num.doubleVal_i());
		EnvisionInt r = EnvisionIntClass.valueOf((long) ceil);
		ret(r);
	}
	
}
