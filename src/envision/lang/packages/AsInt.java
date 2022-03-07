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
		var arg_0 = args[0];
		var num = (Number) EnvisionVariable.convert(arg_0);
		ret(num.intValue());
	}
	
}
