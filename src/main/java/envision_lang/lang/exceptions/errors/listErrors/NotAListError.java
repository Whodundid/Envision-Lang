package envision_lang.lang.exceptions.errors.listErrors;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.exceptions.EnvisionLangError;

public class NotAListError extends EnvisionLangError {

	public NotAListError(EnvisionObject object) {
		super("The given object '" + object + "' is not a list!");
	}

}
