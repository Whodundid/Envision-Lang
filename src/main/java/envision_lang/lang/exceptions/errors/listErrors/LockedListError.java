package envision_lang.lang.exceptions.errors.listErrors;

import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.exceptions.EnvisionLangError;

public class LockedListError extends EnvisionLangError {

	public LockedListError(EnvisionList list) {
		super("Cannot modify list size! The list is size locked!");
	}

}
