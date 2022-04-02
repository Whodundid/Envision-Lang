package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;
import envision.lang.util.EnvisionDatatype;

public class UndefinedTypeError extends EnvisionError {
	
	//public UndefinedTypeError(String type) {
	//	super("The type: " + type + " is undefined within the current scope!");
	//}
	
	public UndefinedTypeError(String message) {
		super(message);
	}
	
	public UndefinedTypeError(EnvisionObject type) {
		this((type != null) ? String.valueOf(type) : "(null)");
	}
	
	public static UndefinedTypeError badType(String typeIn) {
		return new UndefinedTypeError("The type '" + typeIn + "' is not defined within the current scope!");
	}
	
	public static UndefinedTypeError badType(EnvisionDatatype typeIn) {
		return new UndefinedTypeError("The type '" + typeIn + "' is not defined within the current scope!");
	}
	
}
