package envision_lang.exceptions.errors.listErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.EnvisionObject;

public class NotAListError extends EnvisionLangError {

	public NotAListError(EnvisionObject object) {
		super("The given object '" + object + "' is not a list!");
	}

}
