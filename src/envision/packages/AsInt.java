package envision.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class AsInt extends EnvisionFunction {
	
	public AsInt() {
		super(Primitives.INT, "asInt");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(((EnvisionNumber) args[0]).intVal());
	}
	
}
