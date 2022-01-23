package envision.lang.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;

public class Nanos extends EnvisionMethod {
	
	public Nanos() {
		super(EnvisionDataType.INT, "nanos");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		ret(System.nanoTime());
	}
	
}
