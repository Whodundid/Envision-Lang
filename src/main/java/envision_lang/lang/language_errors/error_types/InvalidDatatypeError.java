package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionParameter;
import envision_lang.lang.natives.IDatatype;

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
