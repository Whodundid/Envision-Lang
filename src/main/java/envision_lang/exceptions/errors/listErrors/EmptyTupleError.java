package envision_lang.exceptions.errors.listErrors;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.lang.datatypes.EnvisionTuple;

public class EmptyTupleError extends EnvisionLangError {

	public EmptyTupleError(EnvisionTuple tuple) {
		super("The tuple: '" + tuple + "' is empty!");
	}

}
