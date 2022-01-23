package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import eutil.random.RandomUtil;

public class RandDouble extends EnvisionMethod {
	
	public RandDouble() {
		super(EnvisionDataType.DOUBLE, "randDouble");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ret(RandomUtil.getRoll((double) EnvisionObject.convert(args[0]), (double) EnvisionObject.convert(args[1])));
	}
	
}
