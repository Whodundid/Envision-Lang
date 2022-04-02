package envision.exceptions.errors.listErrors;

import envision.exceptions.EnvisionError;
import envision.lang.datatypes.EnvisionList;

public class LockedListError extends EnvisionError {

	public LockedListError(EnvisionList list) {
		super("The list: '" + list + "' is size locked!");
	}

}
