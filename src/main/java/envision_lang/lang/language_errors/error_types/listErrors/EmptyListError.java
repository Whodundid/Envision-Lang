package envision_lang.lang.language_errors.error_types.listErrors;

import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.language_errors.EnvisionLangError;

public class EmptyListError extends EnvisionLangError {

	public EmptyListError(EnvisionList list) {
		super("The list: '" + list + "' is empty!");
	}

}
