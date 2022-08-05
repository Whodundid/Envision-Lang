package envision_lang.exceptions.errors.listErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.datatypes.EnvisionList;

public class EmptyListError extends EnvisionLangError {

	public EmptyListError(EnvisionList list) {
		super("The list: '" + list + "' is empty!");
	}

}
