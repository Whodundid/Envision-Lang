package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class Floor extends EnvisionFunction {
	
	public Floor() {
		super(Primitives.DOUBLE, "floor");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		Object a = args[0];
		a = (a instanceof EnvisionVariable) ? ((EnvisionVariable) a).get() : a;
		ret((int) Math.floor(((Number) a).doubleValue()));
	}
	
}
