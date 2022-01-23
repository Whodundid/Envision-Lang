package envision.exceptions.errors;

import envision.exceptions.EnvisionError;

public class NegativeArgumentLengthError extends EnvisionError {

	public NegativeArgumentLengthError(Object theMethod, int got) {
		super("Invalid number of arguments for '" + theMethod + "': Negative argument length! (" + got + ")");
	}

}
