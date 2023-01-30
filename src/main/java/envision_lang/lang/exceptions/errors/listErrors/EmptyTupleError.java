package envision_lang.lang.exceptions.errors.listErrors;

import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.exceptions.EnvisionLangError;

public class EmptyTupleError extends EnvisionLangError {

	public EmptyTupleError(EnvisionTuple tuple) {
		super("The tuple: '" + tuple + "' is empty!");
	}

}
