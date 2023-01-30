package envision_lang.lang.exceptions.errors.listErrors;

import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.exceptions.EnvisionLangError;

public class EmptyListError extends EnvisionLangError {

	public EmptyListError(EnvisionList list) {
		super("The list: '" + list + "' is empty!");
	}

}
