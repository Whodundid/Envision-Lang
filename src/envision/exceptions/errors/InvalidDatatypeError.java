package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.natives.IDatatype;
import envision.lang.util.Parameter;

public class InvalidDatatypeError extends EnvisionError {
	
	public InvalidDatatypeError(Parameter expected, Parameter got) {
		super("Expected a '" + expected.datatype + "' but expected '" + got.datatype + "' !");
	}
	
	public InvalidDatatypeError(IDatatype expected, IDatatype got) {
		super("Expected a '" + expected + "' but got '" + got + "' instead!");
	}
	
	/**
	 * Custom message construtor.
	 * 
	 * @param in Custom message
	 */
	public InvalidDatatypeError(String in) {
		super(in);
	}
	
}
