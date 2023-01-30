package envision_lang.lang.exceptions.errors;

import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.util.EnvisionParameter;

public class InvalidDatatypeError extends EnvisionLangError {
	
	public InvalidDatatypeError(EnvisionParameter expected, EnvisionParameter got) {
		super("Expected a '" + expected.datatype + "' but expected '" + got.datatype + "' !");
	}
	
	public InvalidDatatypeError(IDatatype expected, IDatatype got) {
		super("Expected a '" + expected + "' but got '" + got + "' instead!");
	}
	
	/**
	 * Custom message constructor.
	 * 
	 * @param in Custom message
	 */
	public InvalidDatatypeError(String in) {
		super(in);
	}
	
}
