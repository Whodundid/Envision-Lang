package envision_lang.lang.language_errors.error_types.listErrors;

import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.language_errors.EnvisionLangError;

public class LockedListError extends EnvisionLangError {

	public LockedListError(EnvisionList list) {
		super("Cannot modify list size! The list is size locked!");
	}

}
