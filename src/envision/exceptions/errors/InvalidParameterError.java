package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import eutil.debug.Unused;

@Unused
public class InvalidParameterError extends EnvisionError {

	public InvalidParameterError(String in) {
		super(in);
	}
	
}
