package envision_lang.lang.packages.native_packages.debug;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class AssertTrue extends EnvisionFunction {
	
	public AssertTrue() {
		super(EnvisionStaticTypes.VOID_TYPE, "assertTrue", EnvisionStaticTypes.BOOL_TYPE);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		for (EnvisionObject o : args) {
		    if (!interpreter.isTrue(o)) {
		        throw new EnvisionLangError("Envision assertion error! Expected 'TRUE' but got '" + o + "' instead!");
		    }
		}
	}
	
}
