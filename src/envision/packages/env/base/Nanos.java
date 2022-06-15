package envision.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.StaticTypes;

public class Nanos extends EnvisionFunction {
	
	public Nanos() {
		super(StaticTypes.INT_TYPE, "nanos");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(EnvisionIntClass.newInt(System.nanoTime()));
	}
	
}
