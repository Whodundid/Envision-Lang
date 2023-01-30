package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;
import eutil.debug.Unused;

@Unused
public class InvalidParameterError extends EnvisionLangError {

	public InvalidParameterError(String in) {
		super(in);
	}
	
}
