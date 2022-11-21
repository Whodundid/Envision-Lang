package envision_lang.packages.env.base;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.internal.EnvisionFunction;
import envision_lang.lang.natives.StaticTypes;

/**
 * Returns true if the given class instance supports the given operator overload.
 * Takes in an instance of an EnvisionClass and an operator.
 */
public class SupportsOP extends EnvisionFunction {
	
	public SupportsOP() {
		super(StaticTypes.BOOL_TYPE, "supportsOP");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		System.out.println(args);
	}
	
}
