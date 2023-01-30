package envision_lang.lang.packages;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.Primitives;

public class AsInt extends EnvisionFunction {
	
	public AsInt() {
		super(Primitives.INT, "asInt");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(((EnvisionNumber) args[0]).intVal());
	}
	
}
