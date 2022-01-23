package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import eutil.strings.StringUtil;

public class NotAVariableError extends EnvisionError {
	
	public NotAVariableError(Object o) { this(StringUtil.toString(o)); }
	public NotAVariableError(EnvisionObject o) { this(o.getName()); }
	public NotAVariableError(String obj) {
		super("The object '" + obj + "' is not a variable!");
	}
	
}
