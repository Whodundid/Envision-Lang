package envision.exceptions.errors.workingDirectory;

import envision.exceptions.EnvisionError;

public class InvalidCodeFileError extends EnvisionError {
	
	public InvalidCodeFileError(String fileName, String error) {
		super ("The code file: '" + fileName + "' contains the following issues: " + error);
	}
	
}
