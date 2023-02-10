package envision_lang.lang.packages.native_packages.base;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class Sleep extends EnvisionFunction {
	
	public Sleep() {
		super(EnvisionStaticTypes.VOID_TYPE, "sleep");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		try {
			long amount = ((EnvisionInt) args[0]).get_i();
			Thread.sleep(amount);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
