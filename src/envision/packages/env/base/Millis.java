package envision.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.StaticTypes;

public class Millis extends EnvisionFunction {
	
	public Millis() {
		super(StaticTypes.INT_TYPE, "millis");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(EnvisionIntClass.newInt(System.currentTimeMillis()));
	}
	
}
