package envision_lang.lang.language_errors.error_types.listErrors;

import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.language_errors.EnvisionLangError;

public class SelfAdditionError extends EnvisionLangError {

	public SelfAdditionError(EnvisionList list) {
		super("The list: '" + list + "' cannot be added to itself!");
	}

}
