package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import eutil.random.RandomUtil;

public class RandInt extends EnvisionMethod {
	
	public RandInt() {
		super(EnvisionDataType.INT, "randInt");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		//no bounds -- literally any long value from Long.min - Long.max
		if (args.length == 0) {
			ret(RandomUtil.getRoll(Long.MIN_VALUE, Long.MAX_VALUE));
		}
		//only one bound -- assuming min bound is 0 and max is user defined -- if user defined is negative, assume min bound is Long.min
		if (args.length == 1) {
			long upper = (long) convert(args[0]);
			long lower = (upper <= 0) ? Long.MIN_VALUE : 0;
			long val = RandomUtil.getRoll(lower, upper);
			ret(val);
		}
		//user defined bounds -- random number between bounds set by user
		else {
			ret(RandomUtil.getRoll((long) convert(args[0]), (long) convert(args[1])));
		}
	}
	
}
