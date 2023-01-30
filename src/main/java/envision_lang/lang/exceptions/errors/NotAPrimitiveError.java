package envision_lang.lang.exceptions.errors;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.EnvisionLangError;

public class NotAPrimitiveError extends EnvisionLangError {
	
	public NotAPrimitiveError(EnvisionObject o) {
		this("The object '" + o + "' is not a primitive!");
	}
	
	public NotAPrimitiveError(String msg) {
		super(msg);
	}
	
}
