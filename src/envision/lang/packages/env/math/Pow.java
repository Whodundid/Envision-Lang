package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class Pow extends EnvisionFunction {
	
	public Pow() {
		super(Primitives.INT, "pow");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		Object a = args[0];
		Object b = args[1];
		
		a = (a instanceof EnvisionVariable) ? ((EnvisionVariable) a).get() : a;
		b = (b instanceof EnvisionVariable) ? ((EnvisionVariable) b).get() : b;
		ret((int) Math.pow(((Number) a).doubleValue(), ((Number) b).doubleValue()));
	}
	
}
