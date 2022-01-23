package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import eutil.random.RandomUtil;

public class RandName extends EnvisionMethod {
	
	public RandName() {
		super(EnvisionDataType.STRING, "randName");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ret(RandomUtil.randomName());
	}
	
}
