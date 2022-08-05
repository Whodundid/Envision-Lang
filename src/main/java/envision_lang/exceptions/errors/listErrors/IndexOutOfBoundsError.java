package envision_lang.exceptions.errors.listErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.datatypes.EnvisionList;

public class IndexOutOfBoundsError extends EnvisionLangError {

	public IndexOutOfBoundsError(int index) {
		super("The index: '" + index + "' is out of bounds!");
	}
	
	public IndexOutOfBoundsError(int index, EnvisionList list) {
		super("The index: '" + index + "' is out of bounds! [0," + list.size() + "]");
	}

}
