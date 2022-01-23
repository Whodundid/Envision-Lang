package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionVariable;

public class Pow extends EnvisionMethod {
	
	public Pow() {
		super(EnvisionDataType.INT, "pow");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		Object a = args[0];
		Object b = args[1];
		
		a = (a instanceof EnvisionVariable) ? ((EnvisionVariable) a).get() : a;
		b = (b instanceof EnvisionVariable) ? ((EnvisionVariable) b).get() : b;
		ret((int) Math.pow(((Number) a).doubleValue(), ((Number) b).doubleValue()));
	}
	
}
