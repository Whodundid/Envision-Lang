package envision_lang.exceptions.errors.workingDirectory;

import envision_lang.exceptions.EnvisionLangError;

public class InterpreterCreationError extends EnvisionLangError {

	public InterpreterCreationError() {
		super("Failed to create the EnvisionInterpreter!");
	}
	
}
