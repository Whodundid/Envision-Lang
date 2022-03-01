package envision.lang.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

public class Millis extends EnvisionFunction {
	
	//I might need to use Strings to handle internal object types..
	
	public Millis() {
		super(Primitives.INT, "millis");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		ret(System.currentTimeMillis());
	}
	
}
