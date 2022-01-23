package envision.exceptions.errors.listErrors;

import envision.exceptions.EnvisionError;
import envision.lang.objects.EnvisionList;

public class IndexOutOfBoundsError extends EnvisionError {

	public IndexOutOfBoundsError(int index) {
		super("The index: '" + index + "' is out of bounds!");
	}
	
	public IndexOutOfBoundsError(int index, EnvisionList list) {
		super("The index: '" + index + "' is out of bounds! [0," + list.size() + "]");
	}

}
