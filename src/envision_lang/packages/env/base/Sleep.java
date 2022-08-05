package envision_lang.packages.env.base;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.util.StaticTypes;

public class Sleep extends EnvisionFunction {
	
	public Sleep() {
		super(StaticTypes.VOID_TYPE, "sleep");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		try {
			long amount = ((EnvisionInt) args[0]).int_val;
			Thread.sleep(amount);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
