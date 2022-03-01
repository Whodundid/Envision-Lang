package envision.lang.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class Sleep extends EnvisionFunction {
	
	//I might need to use Strings to handle internal object types..
	
	public Sleep() {
		super(Primitives.VOID, "sleep");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		try {
			Thread.sleep((long) args[0]);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
