package envision_lang.packages.env.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.util.StaticTypes;

public class Ceil extends EnvisionFunction {
	
	public Ceil() {
		super(StaticTypes.INT_TYPE, "ceil", StaticTypes.NUMBER_TYPE);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionNumber num = (EnvisionNumber) args[0];
		double ceil = Math.ceil(num.doubleVal_i());
		EnvisionInt r = EnvisionIntClass.newInt(ceil);
		ret(r);
	}
	
}
