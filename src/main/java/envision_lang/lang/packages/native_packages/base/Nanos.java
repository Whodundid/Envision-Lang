package envision_lang.lang.packages.native_packages.base;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class Nanos extends EnvisionFunction {
	
	public Nanos() {
		super(EnvisionStaticTypes.INT_TYPE, "nanos");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(EnvisionIntClass.valueOf(System.nanoTime()));
	}
	
}
