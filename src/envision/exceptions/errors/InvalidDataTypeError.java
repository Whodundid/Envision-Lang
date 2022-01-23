package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.util.EnvisionDataType;
import envision.lang.util.data.Parameter;

public class InvalidDataTypeError extends EnvisionError {

	public InvalidDataTypeError(EnvisionDataType dataType) {
		super(dataType + " is an invalid dataType!");
	}
	
	public InvalidDataTypeError(Parameter got, Parameter expected) {
		super("Invalid type: '" + got.datatype + "' but expected '" + expected.datatype + "' !");
	}
	
	public InvalidDataTypeError(String in) {
		super(in);
	}
	
}
