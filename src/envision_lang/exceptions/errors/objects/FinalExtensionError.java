package envision_lang.exceptions.errors.objects;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class FinalExtensionError extends EnvisionLangError {
	
	public FinalExtensionError(EnvisionObject in) {
		super("The given extension target: '" + in + "' is final and cannot be extended!");
	}

}
