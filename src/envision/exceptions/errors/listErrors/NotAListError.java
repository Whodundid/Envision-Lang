package envision.exceptions.errors.listErrors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class NotAListError extends EnvisionError {

	public NotAListError(EnvisionObject object) {
		super("The given object '" + object + "' is not a list!");
	}

}
