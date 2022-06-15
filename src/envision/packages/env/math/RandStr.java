package envision.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.natives.Primitives;
import eutil.random.RandomUtil;

public class RandStr extends EnvisionFunction {
	
	public RandStr() {
		super(Primitives.STRING, "randStr");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionString s = EnvisionStringClass.newString(RandomUtil.randomString());
		ret(s);
	}
	
}
