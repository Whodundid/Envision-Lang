package envision_lang.exceptions.errors;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang.exceptions.EnvisionLangError;

/** Error thrown when import statement is attempting to reference itself. */
public class SelfImportError extends EnvisionLangError {
	
	public SelfImportError(String message) {
		super(message);
	}

	public SelfImportError(EnvisionCodeFile file) {
		super(file.getFileName() + " cannot import itself!");
	}
	
}
