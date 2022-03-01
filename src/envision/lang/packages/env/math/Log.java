package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class Log extends EnvisionFunction {
	
	public Log() {
		super(Primitives.DOUBLE, "log");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		switch (args.length) {
		case 0:
		case 1:
			ret(Math.log10(((Number) EnvisionVariable.convert(args[0])).doubleValue()));
		case 2:
			Object a = args[0];
			Object b = args[1];
			double ca = ((Number) EnvisionObject.convert(a)).doubleValue();
			double cb = ((Number) EnvisionObject.convert(b)).doubleValue();
			ret(Math.log10(ca / cb));
		}
	}
	
}
