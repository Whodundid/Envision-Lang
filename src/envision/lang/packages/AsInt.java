package envision.lang.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionVariable;

public class AsInt extends EnvisionMethod {
	
	public AsInt() {
		super(EnvisionDataType.INT, "asInt");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ret(((Number) EnvisionVariable.convert(args[0])).intValue());
	}
	
}
