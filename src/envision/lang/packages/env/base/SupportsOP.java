package envision.lang.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionMethod;
import envision.lang.util.EnvisionDataType;

/**
 * Returns true if the given class instance supports the given operator overload.
 * Takes in an instance of an EnvisionClass and an operator.
 */
public class SupportsOP extends EnvisionMethod {
	
	public SupportsOP() {
		super(EnvisionDataType.BOOLEAN, "supportsOP");
	}
	
	@Override
	public void call(EnvisionInterpreter interpreter, Object[] args) {
		System.out.println(args);
	}
	
}
