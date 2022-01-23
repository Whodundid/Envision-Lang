package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.enums.EnvisionEnum;

public class EnumReassignmentError extends EnvisionError {
	
	public EnumReassignmentError(EnvisionEnum var, Object value) {
		super("Attempted to reassign the value of an enum: " + var.getName() + " to " + value + "!");
	}
	
}
