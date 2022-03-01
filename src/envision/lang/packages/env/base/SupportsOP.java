package envision.lang.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.objects.EnvisionFunction;
import envision.lang.util.Primitives;

/**
 * Returns true if the given class instance supports the given operator overload.
 * Takes in an instance of an EnvisionClass and an operator.
 */
public class SupportsOP extends EnvisionFunction {
	
	public SupportsOP() {
		super(Primitives.BOOLEAN, "supportsOP");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, Object[] args) {
		System.out.println(args);
	}
	
}
