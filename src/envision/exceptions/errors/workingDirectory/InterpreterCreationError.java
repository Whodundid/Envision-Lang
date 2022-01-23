package envision.exceptions.errors.workingDirectory;

import envision.exceptions.EnvisionError;

public class InterpreterCreationError extends EnvisionError {

	public InterpreterCreationError() {
		super("Failed to create the EnvisionInterpreter!");
	}
	
}
