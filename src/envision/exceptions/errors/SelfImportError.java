package envision.exceptions.errors;

import envision._launch.EnvisionCodeFile;
import envision.exceptions.EnvisionError;

/** Error thrown when import statement is attempting to reference itself. */
public class SelfImportError extends EnvisionError {
	
	public SelfImportError(String message) {
		super(message);
	}

	public SelfImportError(EnvisionCodeFile file) {
		super(file.getFileName() + " cannot import itself!");
	}
	
}
