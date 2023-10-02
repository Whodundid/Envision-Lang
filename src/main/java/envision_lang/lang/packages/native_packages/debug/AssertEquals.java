package envision_lang.lang.packages.native_packages.debug;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.functions.EnvisionFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;

public class AssertEquals extends EnvisionFunction {
	
	public AssertEquals() {
		super(EnvisionStaticTypes.VOID_TYPE, "assertEquals", EnvisionStaticTypes.VAR_TYPE, EnvisionStaticTypes.VAR_TYPE);
	}
	
	@Override
	public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
	    if (args.length != 2) {
	        throw new EnvisionLangError("Envision assertion error! Expected exectly '2' arguments for assertEquals!");
	    }
	    
	    EnvisionObject expected = args[0];
	    EnvisionObject got = args[1];
	    
	    if (!interpreter.isEqual_i(expected, got)) {
            throw new EnvisionLangError("Envision assertion error! Expected '" + expected + "' but got '" + got + "' instead!");
        }
	}
	
}
