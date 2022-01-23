package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;

public class Sqrt extends EnvisionMethod {
	
	public Sqrt() {
		super(EnvisionDataType.DOUBLE, "sqrt");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ret(Math.sqrt(((Number) args[0]).doubleValue()));
	}
	
}
