package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;

public class NegativeArgumentLengthError extends EnvisionLangError {

	public NegativeArgumentLengthError(Object theMethod, int got) {
		super("Invalid number of arguments for '" + theMethod + "': Negative argument length! (" + got + ")");
	}

}
