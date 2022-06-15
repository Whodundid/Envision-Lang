package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

/**
 * An error thrown if null is returned for a variable call.
 * <p>
 * Envision:Java does <u><strong>NOT</strong></u> allow Java::null as a language
 * value due to the fact that it would be generally very difficult to accurately
 * distinguish between actual Java::null and logical Envision::null values.
 * Furthermore, the presence of Java::null within Envision is almost always
 * indicative of either a logic, casting, or creation error which occurred at
 * some earlier point during program execution.
 */
public class NullVariableError extends EnvisionError {
	
	public NullVariableError() {
		super("This object is entirely Null");
	}
	
	public NullVariableError(String varName) {
		super("Variable '" + varName + "' has not been declared within this scope!");
	}
	
	public NullVariableError(EnvisionObject obj) {
		super("The given object '" + obj + "' is null!");
	}
	
}
