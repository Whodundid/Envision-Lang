package envision_lang.lang.language_errors.error_types.listErrors;

import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.language_errors.EnvisionLangError;

public class EmptyTupleError extends EnvisionLangError {

	public EmptyTupleError(EnvisionTuple tuple) {
		super("The tuple: '" + tuple + "' is empty!");
	}

}
