package envision.lang.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;
import eutil.random.RandomUtil;

public class RandStr extends EnvisionMethod {
	
	public RandStr() {
		super(EnvisionDataType.STRING, "randStr");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ret(RandomUtil.randomString());
	}
	
}
