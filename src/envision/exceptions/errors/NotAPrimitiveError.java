package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class NotAPrimitiveError extends EnvisionError {
	
	public NotAPrimitiveError(EnvisionObject o) {
		this("The object '" + o + "' is not a primitive!");
	}
	
	public NotAPrimitiveError(String msg) {
		super(msg);
	}
	
}
