package envision.exceptions.errors.listErrors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionList;

public class SelfAdditionError extends EnvisionError {

	public SelfAdditionError(EnvisionList list) {
		super("The list: '" + list.getName() + "' cannot be added to itself!");
	}

}
