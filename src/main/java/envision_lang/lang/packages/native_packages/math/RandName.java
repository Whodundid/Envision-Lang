package envision_lang.lang.packages.native_packages.math;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.Primitives;
import eutil.random.ERandomUtil;

public class RandName extends EnvisionFunction {
	
	public RandName() {
		super(Primitives.STRING, "randName");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionString s = EnvisionStringClass.valueOf(ERandomUtil.randomName());
		ret(s);
	}
	
}