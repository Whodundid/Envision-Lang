package envision.exceptions.errors.listErrors;

import envision.exceptions.EnvisionError;
import envision.lang.datatypes.EnvisionList;

public class EmptyListError extends EnvisionError {

	public EmptyListError(EnvisionList list) {
		super("The list: '" + list + "' is empty!");
	}

}
