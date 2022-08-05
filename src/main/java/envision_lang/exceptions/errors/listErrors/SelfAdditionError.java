package envision_lang.exceptions.errors.listErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.datatypes.EnvisionList;

public class SelfAdditionError extends EnvisionLangError {

	public SelfAdditionError(EnvisionList list) {
		super("The list: '" + list + "' cannot be added to itself!");
	}

}
