package envision.exceptions.errors.listErrors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionList;

public class EmptyListError extends EnvisionError {

	public EmptyListError(EnvisionList list) {
		super("The list: '" + list.getName() + "' is empty!");
	}

}
