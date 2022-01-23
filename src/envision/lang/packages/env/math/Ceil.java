package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionVariable;

public class Ceil extends EnvisionMethod {
	
	public Ceil() {
		super(EnvisionDataType.DOUBLE, "ceil");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		Object a = args[0];
		a = (a instanceof EnvisionVariable) ? ((EnvisionVariable) a).get() : a;
		ret((int) Math.ceil(((Number) a).doubleValue()));
	}
	
}
