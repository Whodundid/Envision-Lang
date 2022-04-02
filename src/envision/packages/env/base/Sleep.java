package envision.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.internal.EnvisionFunction;
import envision.lang.util.Primitives;

public class Sleep extends EnvisionFunction {
	
	public Sleep() {
		super(Primitives.VOID, "sleep");
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
