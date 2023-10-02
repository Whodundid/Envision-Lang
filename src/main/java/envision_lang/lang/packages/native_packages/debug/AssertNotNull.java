package envision_lang.lang.packages.native_packages.debug;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class AssertNotNull extends EnvisionFunction {
	
	public AssertNotNull() {
		super(EnvisionStaticTypes.VOID_TYPE, "assertNotNull", EnvisionStaticTypes.VAR_TYPE);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		for (EnvisionObject o : args) {
		    if (interpreter.isEqual_i(EnvisionNull.NULL, o)) {
		        throw new EnvisionLangError("Envision assertion error! Expected value to not be 'NULL' but it was!");
		    }
		}
	}
	
}
