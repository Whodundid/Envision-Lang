package envision_lang.lang.exceptions.errors.workingDirectory;

import envision_lang.lang.exceptions.EnvisionLangError;

public class InterpreterCreationError extends EnvisionLangError {

	public InterpreterCreationError() {
		super("Failed to create the EnvisionInterpreter!");
	}
	
}
