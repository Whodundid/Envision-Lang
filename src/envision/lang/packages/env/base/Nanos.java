package envision.lang.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class Nanos extends EnvisionFunction {
	
	public Nanos() {
		super(Primitives.INT, "nanos");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		ret(System.nanoTime());
	}
	
}
