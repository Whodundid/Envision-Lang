package envision.lang.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionVariable;

public class AsDouble extends EnvisionMethod {
	
	public AsDouble() {
		super(EnvisionDataType.INT, "asDouble");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ret(((Number) EnvisionVariable.convert(args[0])).doubleValue());
	}
	
}
