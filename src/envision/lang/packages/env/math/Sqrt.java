package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class Sqrt extends EnvisionFunction {
	
	public Sqrt() {
		super(Primitives.DOUBLE, "sqrt");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		ret(Math.sqrt(((Number) args[0]).doubleValue()));
	}
	
}
