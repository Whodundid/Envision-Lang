package envision_lang.lang.packages.native_packages.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import eutil.random.ERandomUtil;

public class RandStr extends EnvisionFunction {
	
	public RandStr() {
		super(Primitives.STRING, "randStr");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionString s = EnvisionStringClass.valueOf(ERandomUtil.randomString());
		ret(s);
	}
	
}
