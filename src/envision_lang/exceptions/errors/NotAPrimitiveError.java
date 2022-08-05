package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class NotAPrimitiveError extends EnvisionLangError {
	
	public NotAPrimitiveError(EnvisionObject o) {
		this("The object '" + o + "' is not a primitive!");
	}
	
	public NotAPrimitiveError(String msg) {
		super(msg);
	}
	
}
