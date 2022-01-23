package envision.exceptions.errors;

import envision.exceptions.EnvisionError;
import envision.lang.EnvisionObject;

public class NotVisibleError extends EnvisionError {

	public NotVisibleError(EnvisionObject object) {
		super("The member: '" + object.getName() + "' is not visible within the current scope!");
	}
	
}
