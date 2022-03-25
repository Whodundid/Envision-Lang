package envision.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionDouble;
import envision.lang.datatypes.EnvisionDoubleClass;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class Sqrt extends EnvisionFunction {
	
	public Sqrt() {
		super(Primitives.DOUBLE, "sqrt", Primitives.NUMBER);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionNumber num = (EnvisionNumber) args[0];
		EnvisionDouble sqrt = EnvisionDoubleClass.newDouble(Math.sqrt(num.doubleVal_i()));
		ret(sqrt);
	}
	
}
