package envision.lang.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;

public class Millis extends EnvisionMethod {
	
	//I might need to use Strings to handle internal object types..
	
	public Millis() {
		super(EnvisionDataType.INT, "millis");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ret(System.currentTimeMillis());
	}
	
}
