package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class UndefinedTypeError extends EnvisionError {
	
	//public UndefinedTypeError(String type) {
	//	super("The type: " + type + " is undefined within the current scope!");
	//}
	
	public UndefinedTypeError(String message) {
		super(message);
	}
	
	public UndefinedTypeError(EnvisionObject type) {
		this((type != null) ? type.getName() : "(null)");
	}
	
}
