package envision.lang.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;

public class Sleep extends EnvisionMethod {
	
	//I might need to use Strings to handle internal object types..
	
	public Sleep() {
		super(EnvisionDataType.VOID, "sleep");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		try {
			Thread.sleep((long) args[0]);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
