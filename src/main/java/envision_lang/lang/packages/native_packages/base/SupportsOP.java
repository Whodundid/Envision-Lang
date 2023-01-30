package envision_lang.lang.packages.native_packages.base;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.EnvisionFunction;
import envision_lang.lang.natives.EnvisionStaticTypes;

/**
 * Returns true if the given class instance supports the given operator overload.
 * Takes in an instance of an EnvisionClass and an operator.
 */
public class SupportsOP extends EnvisionFunction {
	
	public SupportsOP() {
		super(EnvisionStaticTypes.BOOL_TYPE, "supportsOP");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		System.out.println(args);
	}
	
}
