package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import eutil.debug.Unused;

@Unused
public class NotAVariableError extends EnvisionError {
	
	public NotAVariableError(Object o) { this(String.valueOf(o)); }
	public NotAVariableError(EnvisionObject o) { this(String.valueOf(o)); }
	public NotAVariableError(String obj) {
		super("The object '" + obj + "' is not a variable!");
	}
	
}
