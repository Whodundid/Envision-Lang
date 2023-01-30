package envision_lang.lang.packages.native_packages.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.datatypes.EnvisionDoubleClass;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.Primitives;

public class Sqrt extends EnvisionFunction {
	
	public Sqrt() {
		super(Primitives.DOUBLE, "sqrt", Primitives.NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionNumber num = (EnvisionNumber) args[0];
		EnvisionDouble sqrt = EnvisionDoubleClass.valueOf(Math.sqrt(num.doubleVal_i()));
		ret(sqrt);
	}
	
}
