package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;
import eutil.random.RandomUtil;

public class RandDouble extends EnvisionFunction {
	
	public RandDouble() {
		super(Primitives.DOUBLE, "randDouble");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		ret(RandomUtil.getRoll((double) EnvisionObject.convert(args[0]), (double) EnvisionObject.convert(args[1])));
	}
	
}
