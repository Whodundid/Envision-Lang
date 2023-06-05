package envision_lang.lang.packages.native_packages.debug;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class DebugStackFrames extends EnvisionFunction {
	
	public DebugStackFrames() {
		super(EnvisionStaticTypes.VOID_TYPE, "stackFrames");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		interpreter.printStackFrames();
	}
	
}
