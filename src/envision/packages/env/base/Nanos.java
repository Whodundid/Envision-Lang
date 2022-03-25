package envision.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class Nanos extends EnvisionFunction {
	
	public Nanos() {
		super(Primitives.INT, "nanos");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(EnvisionIntClass.newInt(System.nanoTime()));
	}
	
}
