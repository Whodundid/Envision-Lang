package envision_lang.exceptions.errors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.natives.IDatatype;

public class UndefinedTypeError extends EnvisionLangError {
	
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
	
	public static UndefinedTypeError badType(IDatatype typeIn) {
		return new UndefinedTypeError("The type '" + typeIn + "' is not defined within the current scope!");
	}
	
}
