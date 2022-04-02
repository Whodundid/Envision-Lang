package envision.packages.env.base;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.internal.EnvisionFunction;
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
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		System.out.println(args);
	}
	
}