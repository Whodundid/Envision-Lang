package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;

public class NegativeArgumentLengthError extends EnvisionLangError {

	public NegativeArgumentLengthError(Object theMethod, int got) {
		super("Invalid number of arguments for '" + theMethod + "': Negative argument length! (" + got + ")");
	}

}
