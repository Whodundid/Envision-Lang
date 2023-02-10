package envision_lang.lang.language_errors.error_types.workingDirectory;

import envision_lang._launch.WorkingDirectory;
import envision_lang.lang.language_errors.EnvisionLangError;

public class BadDirError extends EnvisionLangError {
	
	public BadDirError(WorkingDirectory dir) {
		super ("The working directory: '" + dir.getDirFile() + "' is not a valid directory!");
	}
	
}
