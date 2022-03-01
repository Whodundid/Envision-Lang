package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;
import eutil.random.RandomUtil;

public class RandStr extends EnvisionFunction {
	
	public RandStr() {
		super(Primitives.STRING, "randStr");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		ret(RandomUtil.randomString());
	}
	
}
