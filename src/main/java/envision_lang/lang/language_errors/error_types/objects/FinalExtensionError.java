package envision_lang.lang.language_errors.error_types.objects;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;

public class FinalExtensionError extends EnvisionLangError {
	
	public FinalExtensionError(EnvisionObject in) {
		super("The given extension target: '" + in + "' is final and cannot be extended!");
	}

}
