package envision_lang.lang.language_errors.error_types.workingDirectory;

import envision_lang.lang.language_errors.EnvisionLangError;

public class InterpreterCreationError extends EnvisionLangError {

	public InterpreterCreationError() {
		super("Failed to create the EnvisionInterpreter!");
	}
	
}
