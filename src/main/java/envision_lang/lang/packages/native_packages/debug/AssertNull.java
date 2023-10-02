package envision_lang.lang.packages.native_packages.debug;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class AssertNull extends EnvisionFunction {
	
	public AssertNull() {
		super(EnvisionStaticTypes.VOID_TYPE, "assertNull");
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		for (EnvisionObject o : args) {
		    if (o != EnvisionNull.NULL) {
		        throw new EnvisionLangError("Envision assertion error! Expected 'NULL' but got '" + o + "' instead!");
		    }
		}
	}
	
}
