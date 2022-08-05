package envision_lang.exceptions.errors.workingDirectory;

import envision_lang._launch.WorkingDirectory;
import envision_lang.exceptions.EnvisionLangError;

public class BadDirError extends EnvisionLangError {
	
	public BadDirError(WorkingDirectory dir) {
		super ("The working directory: '" + dir.getDirFile() + "' is not a valid directory!");
	}
	
}
