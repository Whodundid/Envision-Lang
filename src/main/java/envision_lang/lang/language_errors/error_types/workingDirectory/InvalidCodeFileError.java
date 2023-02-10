package envision_lang.lang.language_errors.error_types.workingDirectory;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.lang.language_errors.EnvisionLangError;

public class InvalidCodeFileError extends EnvisionLangError {
	
	public InvalidCodeFileError(EnvisionCodeFile file) {
		super("The code file '" + file.getFileName() + "' cannot be executed as it has invalid parameters!");
	}
	
	public InvalidCodeFileError(String fileName, String error) {
		super("The code file '" + fileName + "' contains the following issues: " + error);
	}
	
}
