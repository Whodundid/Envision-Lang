package envision_lang.packages.env.base;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.util.StaticTypes;

public class Nanos extends EnvisionFunction {
	
	public Nanos() {
		super(StaticTypes.INT_TYPE, "nanos");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(EnvisionIntClass.newInt(System.nanoTime()));
	}
	
}
