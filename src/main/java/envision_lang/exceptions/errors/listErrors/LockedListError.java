package envision_lang.exceptions.errors.listErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.datatypes.EnvisionList;

public class LockedListError extends EnvisionLangError {

	public LockedListError(EnvisionList list) {
		super("The list: '" + list + "' is size locked!");
	}

}
