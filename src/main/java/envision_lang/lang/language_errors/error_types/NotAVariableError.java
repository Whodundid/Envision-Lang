package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;
import eutil.debug.Unused;

@Unused
public class NotAVariableError extends EnvisionLangError {
	
	public NotAVariableError(Object o) { this(String.valueOf(o)); }
	public NotAVariableError(EnvisionObject o) { this(String.valueOf(o)); }
	public NotAVariableError(String obj) {
		super("The object '" + obj + "' is not a variable!");
	}
	
}
