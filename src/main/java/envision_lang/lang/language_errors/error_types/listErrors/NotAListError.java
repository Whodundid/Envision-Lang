package envision_lang.lang.language_errors.error_types.listErrors;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;

public class NotAListError extends EnvisionLangError {

	public NotAListError(EnvisionObject object) {
		super("The given object '" + object + "' is not a list!");
	}

}
