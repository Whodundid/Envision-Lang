package envision.exceptions.errors.workingDirectory;

import envision._launch.EnvisionCodeFile;
import envision.exceptions.EnvisionError;

public class InvalidCodeFileError extends EnvisionError {
	
	public InvalidCodeFileError(EnvisionCodeFile file) {
		super("The code file '" + file.getFileName() + "' cannot be executed as it has invalid parameters!");
	}
	
	public InvalidCodeFileError(String fileName, String error) {
		super("The code file '" + fileName + "' contains the following issues: " + error);
	}
	
}
