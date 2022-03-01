package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;
import eutil.random.RandomUtil;

public class RandName extends EnvisionFunction {
	
	public RandName() {
		super(Primitives.STRING, "randName");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		ret(RandomUtil.randomName());
	}
	
}
