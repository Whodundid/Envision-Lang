package envision_lang.lang.exceptions.errors.listErrors;

import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.exceptions.EnvisionLangError;

public class IndexOutOfBoundsError extends EnvisionLangError {

	public IndexOutOfBoundsError(int index) {
		super("The index: '" + index + "' is out of bounds!");
	}
	
	public IndexOutOfBoundsError(int index, EnvisionList list) {
		super("The index: '" + index + "' is out of bounds! [0," + list.size() + "]");
	}
	
	public IndexOutOfBoundsError(int index, EnvisionTuple tuple) {
		super("The index: '" + index + "' is out of bounds! [0," + tuple.size() + "]");
	}

}
