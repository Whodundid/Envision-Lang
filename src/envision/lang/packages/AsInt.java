package envision.lang.packages;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class AsInt extends EnvisionFunction {
	
	public AsInt() {
		super(Primitives.INT, "asInt");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		ret(((Number) EnvisionVariable.convert(args[0])).intValue());
	}
	
}
