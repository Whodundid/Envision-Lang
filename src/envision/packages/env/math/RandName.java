package envision.packages.env.math;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;
import eutil.random.RandomUtil;

public class RandName extends EnvisionFunction {
	
	public RandName() {
		super(Primitives.STRING, "randName");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionString s = EnvisionStringClass.newString(RandomUtil.randomName());
		ret(s);
	}
	
}
