package envision_lang.lang.language_errors.error_types;

import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;

public class StrongVarReassignmentError extends EnvisionLangError {
	
	public StrongVarReassignmentError(IDatatype expected, IDatatype got) {
		super("Attempted to reassign the value of a STRONG dynamic variable! Expected: ['" + expected
			  + "' or 'NULL'], but got: '" + got + "' instead!");
	}
	
}
