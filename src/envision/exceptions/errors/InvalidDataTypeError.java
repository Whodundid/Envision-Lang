package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.util.Primitives;
import envision.lang.util.data.Parameter;

public class InvalidDatatypeError extends EnvisionError {

	public InvalidDatatypeError(Primitives dataType) {
		super(dataType + " is an invalid dataType!");
	}
	
	public InvalidDatatypeError(Parameter got, Parameter expected) {
		super("Invalid type: '" + got.datatype + "' but expected '" + expected.datatype + "' !");
	}
	
	public InvalidDatatypeError(String in) {
		super(in);
	}
	
}
