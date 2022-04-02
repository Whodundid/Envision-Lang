package envision.exceptions.errors.listErrors;

import envision.exceptions.EnvisionError;
import envision.lang.datatypes.EnvisionList;

public class SelfAdditionError extends EnvisionError {

	public SelfAdditionError(EnvisionList list) {
		super("The list: '" + list + "' cannot be added to itself!");
	}

}
