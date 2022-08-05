package envision_lang.packages;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionNumber;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.Primitives;

public class AsDouble extends EnvisionFunction {
	
	public AsDouble() {
		super(Primitives.DOUBLE, "asDouble");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		ret(((EnvisionNumber) args[0]).doubleVal());
	}
	
}
