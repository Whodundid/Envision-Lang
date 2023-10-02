package envision_lang.lang.packages.native_packages.debug;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class AssertFalse extends EnvisionFunction {
	
	public AssertFalse() {
		super(EnvisionStaticTypes.VOID_TYPE, "assertFalse", EnvisionStaticTypes.BOOL_TYPE);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		for (EnvisionObject o : args) {
		    if (interpreter.isTrue(o)) {
		        throw new EnvisionLangError("Envision assertion error! Expected 'FALSE' but got '" + o + "' instead!");
		    }
		}
	}
	
}
