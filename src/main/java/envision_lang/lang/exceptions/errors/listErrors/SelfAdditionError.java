package envision_lang.lang.exceptions.errors.listErrors;

import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.exceptions.EnvisionLangError;

public class SelfAdditionError extends EnvisionLangError {

	public SelfAdditionError(EnvisionList list) {
		super("The list: '" + list + "' cannot be added to itself!");
	}

}
