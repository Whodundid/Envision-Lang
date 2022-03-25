package envision.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionNumber;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class AsDouble extends EnvisionFunction {
	
	public AsDouble() {
		super(Primitives.DOUBLE, "asDouble");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(((EnvisionNumber) args[0]).doubleVal());
	}
	
}
